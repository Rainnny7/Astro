package me.braydon.astro.route;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import me.braydon.astro.common.HTTPUtils;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Braydon
 */
@Getter
public class Request {
    private final HttpExchange httpExchange;
    private final Headers headers;
    private final Map<String, String> parameters;
    private final RequestMethod method;

    public Request(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        headers = httpExchange.getRequestHeaders();
        parameters = Collections.unmodifiableMap(HTTPUtils.getParameters(httpExchange.getRequestURI().getQuery()));
        method = RequestMethod.byName(httpExchange.getRequestMethod());
    }

    /**
     * Get the {@link URI} of the request.
     *
     * @return the uri
     */
    public URI getURI() {
        return httpExchange.getRequestURI();
    }

    /**
     * Get the client address that made the request.
     *
     * @return the address
     * @see InetSocketAddress for the address
     */
    public InetSocketAddress getAddress() {
        return httpExchange.getRemoteAddress();
    }

    /**
     * Get the amount of headers provided in the request.
     *
     * @return the count
     */
    public int headerCount() {
        int headers = 0;
        for (List<String> list : this.headers.values()) {
            headers+= list.size();
        }
        return headers;
    }

    /**
     * Get a list of headers with the given name.
     *
     * @param name the name of the headers
     * @return the headers
     */
    public List<String> getHeaders(String name) {
        return headers.get(name);
    }

    /**
     * Get the header with the given name.
     *
     * @param name the name of the header
     * @return the header, null if none
     */
    public String getHeader(String name) {
        return headers.getFirst(name);
    }

    /**
     * Get the amount of parameters provided in the request.
     *
     * @return the count
     */
    public int parameterCount() {
        return parameters.size();
    }

    /**
     * Get the parameter with the given name.
     *
     * @param name the name of the parameter
     * @return the parameter, null if none
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }
}