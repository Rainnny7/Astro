package me.braydon.astro.route;

import lombok.AllArgsConstructor;
import me.braydon.astro.Astro;
import me.braydon.astro.event.AstroEvent;

/**
 * @author Braydon
 */
@AllArgsConstructor @Route
public class DefaultAstroRoute {
    private final Astro astro;

    @RestPath(path = "/", method = RequestMethod.GET)
    public String handle(Request request, Response response) {
        for (AstroEvent event : astro.getEvents()) {
            event.handle(request, response);
        }
        return "Default Astro Route";
    }
}