package me.braydon.astro.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All RESTful paths are annotated with this annotation.
 * <p>
 * This annotation serves the purpose of providing information
 * for each path.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestPath {
    /**
     * The path to use.
     */
    String path();

    /**
     * The request method for this path.
     */
    RequestMethod method();
}