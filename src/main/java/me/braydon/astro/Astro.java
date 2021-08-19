package me.braydon.astro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.braydon.astro.common.AstroReflection;
import me.braydon.astro.event.AstroEvent;
import me.braydon.astro.exception.AstroException;
import me.braydon.astro.exception.RestPathException;
import me.braydon.astro.mysql.MySQLConnector;
import me.braydon.astro.route.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Braydon
 */
@Slf4j(topic = "Astro") @Getter
public class Astro {
    /**
     * The default {@link Gson} object to use.
     * <p>
     * You can provide your own {@link Gson} instance
     * with the {@link AstroBuilder}.
     */
    private static final Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private final int port;
    private final Gson gson;
    private final List<AstroEvent> events;
    private final List<Object> routes;
    private final MySQLConnector mySQLConnector;

    @Getter(AccessLevel.NONE) private HttpServer httpServer;
    private AstroReflection reflection;
    private boolean running;

    private Astro(int port, Gson gson, List<AstroEvent> events, List<Object> routes, MySQLConnector mySQLConnector) {
        this.port = port;
        this.gson = gson;
        this.events = events;
        this.routes = routes;
        this.mySQLConnector = mySQLConnector;

        // Adding the default route
        boolean addDefault = true;
        for (Object route : routes) {
            for (Method method : route.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(RestPath.class)) {
                    continue;
                }
                RestPath path = method.getAnnotation(RestPath.class);
                if (path.path().equals("/")) {
                    addDefault = false;
                    break;
                }
            }
        }
        if (addDefault) {
            this.routes.add(new DefaultAstroRoute(this));
        } else {
            log.warn("Astro was initialized without a default route, events will not be handled");
        }

        // Creating the HTTP server and initializing AstroReflection
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.setExecutor(null);

            reflection = new AstroReflection(httpServer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Start Astro.
     *
     * @throws IllegalStateException if Astro is already running
     */
    public Astro start() {
        if (running) {
            throw new IllegalStateException("Astro is already running");
        }
        running = true;
        httpServer.start();
        for (Object classInstance : routes) {
            addRoute(classInstance);
        }
        log.info("Started listening on port " + port);
        return this;
    }

    /**
     * Stop Astro.
     *
     * @throws IllegalStateException if Astro is not running
     */
    public void cleanup() {
        if (!running) {
            throw new IllegalStateException("Astro is not running");
        }
        running = false;
        httpServer.stop(0);
    }

    /**
     * Add the given route.
     *
     * @param classInstance the instance of the route to add
     */
    private void addRoute(Object classInstance) {
        Class<?> clazz = classInstance.getClass();
        if (!clazz.isAnnotationPresent(Route.class)) {
            throw new IllegalArgumentException("Provided class does not annotate @Route");
        }
        Route route = clazz.getAnnotation(Route.class);
        int pathsAdded = 0;
        for (Method method : clazz.getDeclaredMethods()) {
            // If the method does not have the @RestPath annotation, continue
            if (!method.isAnnotationPresent(RestPath.class)) {
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            // If the method has improperly defined parameters, throw an exception
            if (parameterTypes.length < 2 || (parameterTypes[0] != Request.class || parameterTypes[1] != Response.class)) {
                throw new IllegalArgumentException("Method " + method.getName() + " in route " + clazz.getSimpleName() + " has improperly defined parameters, expected Request and Response");
            }
            RestPath restPath = method.getAnnotation(RestPath.class);

            String path = restPath.path();
            if (!route.path().isEmpty()) {
                path = route.path() + path;
            }

            httpServer.createContext(path, httpExchange -> {
                Request request = new Request(httpExchange);
                log.info("Handling request from " + request.getAddress().toString() + " on " + request.getURI().toString()); // Log the request
                Response response = new Response();

                // Handling events
                for (AstroEvent event : events) {
                    event.handle(request, response);
                }

                JsonObject jsonObject = new JsonObject();
                try {
                    // If the request method is not supported, throw an exception.
                    // This exception will be displayed to the client.
                    if (request.getMethod() == null) {
                        throw new AstroException(String.format("Request method is not supported (supported: %s)",
                                Arrays.stream(RequestMethod.values()).map(RequestMethod::name).collect(Collectors.joining(", "))
                        ));
                    }
                    // Invoke the restPath method and get the result
                    Object returned = method.invoke(classInstance, request, response);

                    // If the method doesn't return void, add it to the json object
                    if (method.getReturnType() != Void.class) {
                        jsonObject.addProperty("success", true);
                        jsonObject.add("value", gson.toJsonTree(returned));
                    } else if (restPath.method() == RequestMethod.GET) { // If the method returns void but the request method is get, throw an exception
                        throw new AstroException("Request method is valid but the restPath returns void");
                    }
                } catch (IllegalAccessException | InvocationTargetException | RestPathException ex) {
                    log.error("Failed handling request:"); // Log the error

                    // As the default response code is 200 (OK), we check if the response code is equal to that
                    // and if it is we set the response code to 500 (INTERNAL_SERVER_ERROR)
                    if (response.getResponseCode() == ResponseCode.OK) {
                        response.responseCode(ResponseCode.INTERNAL_SERVER_ERROR);
                    }
                    jsonObject.addProperty("success", false);
                    jsonObject.addProperty("error", ex.getCause().getLocalizedMessage());
                    ex.printStackTrace();
                }
                String json = gson.toJson(jsonObject); // Turn the json object into a readable json string
                httpExchange.sendResponseHeaders(response.getResponseCode().getCode(), json.length()); // Set the response code and body lengh
                OutputStream responseBody = httpExchange.getResponseBody();
                responseBody.write(json.getBytes()); // Writing the json to the response body
                responseBody.close();
            });
            pathsAdded++;
        }
        // Log that the route was added
        if (pathsAdded == 0) {
            log.warn("Couldn't find any paths to add in route " + clazz.getSimpleName());
        } else {
            log.info("Added " + pathsAdded + " path for route " + clazz.getSimpleName());
        }
    }

    /**
     * Construct a new Astro Builder with the given port.
     *
     * @param port the port
     * @return the builder
     */
    public static AstroBuilder builder(int port) {
        return new AstroBuilder(port);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class AstroBuilder {
        private final int port;
        private Gson gson = DEFAULT_GSON;
        private final List<AstroEvent> events = new ArrayList<>();
        private final List<Object> routes = new ArrayList<>();
        private MySQLConnector mySQLConnector;

        /**
         * Provide your own {@link Gson} provider for Astro to use.
         *
         * @param gson your gson provider
         */
        public AstroBuilder withGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        /**
         * Add the given event.
         *
         * @param event the event to add
         */
        public AstroBuilder addEvent(AstroEvent event) {
            events.add(event);
            return this;
        }

        /**
         * Add the given array of routes.
         *
         * @param classInstances the array of routes to add
         */
        public AstroBuilder addRoutes(Object... classInstances) {
            for (Object classInstance : classInstances) {
                addRoute(classInstance);
            }
            return this;
        }

        /**
         * Add the given route.
         *
         * @param classInstance the instance of the route to add
         */
        public AstroBuilder addRoute(Object classInstance) {
            routes.add(classInstance);
            return this;
        }

        /**
         * Set the connector to use for MySQL.
         *
         * @param mySQLConnector the mysql connector
         */
        public AstroBuilder withMySQLConnector(MySQLConnector mySQLConnector) {
            this.mySQLConnector = mySQLConnector;
            return this;
        }

        /**
         * Build a new instance of Astro.
         *
         * @return the built instance
         */
        public Astro build() {
            return new Astro(port, gson, events, routes, mySQLConnector);
        }
    }
}