package me.braydon.astro.common;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * @author Braydon
 */
public class AstroReflection {
    private Object contexts;
    private Field listField;

    public AstroReflection(HttpServer httpServer) {
        try {
            // I hate the java sun HTTPServer, they love making things private
            Field serverField = httpServer.getClass().getDeclaredField("server");
            serverField.setAccessible(true);

            Object serverImpl = serverField.get(httpServer);
            Field contextsField = serverImpl.getClass().getDeclaredField("contexts");
            contextsField.setAccessible(true);

            contexts = contextsField.get(serverImpl);
            listField = contexts.getClass().getDeclaredField("list");
            listField.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a list of http contexts.
     *
     * @return the contexts
     */
    public LinkedList<HttpContext> getContexts() {
        try {
            return (LinkedList<HttpContext>) listField.get(contexts);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return new LinkedList<>();
    }
}