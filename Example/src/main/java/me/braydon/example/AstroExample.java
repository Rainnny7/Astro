package me.braydon.example;

import me.braydon.astro.Astro;
import me.braydon.example.route.PersonRoute;

/**
 * @author Braydon
 */
public class AstroExample {
    public static void main(String[] args) {
        // Starts the Astro server on port 8080 with the PersonRoute
        Astro astro = Astro.builder(8080)
                .addRoute(new PersonRoute())
                .build().start();

        // Adds a shutdown hook - This ensures that the Astro server shuts down correctly
        // when the application exits.
        Runtime.getRuntime().addShutdownHook(new Thread(astro::cleanup));
    }
}