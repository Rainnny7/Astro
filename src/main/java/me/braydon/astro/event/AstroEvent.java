package me.braydon.astro.event;

import me.braydon.astro.route.Request;
import me.braydon.astro.route.Response;
import me.braydon.astro.route.Route;

/**
 * @author Braydon
 */
public interface AstroEvent {
    /**
     * This method is fired when a http request is made to the server.
     * <p>
     * Events have priority over {@link Route}'s.
     *
     * @param request the request
     * @param response the response
     */
    void handle(Request request, Response response);
}