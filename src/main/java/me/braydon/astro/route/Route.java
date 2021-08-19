package me.braydon.astro.route;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Route {
    /**
     * The parent path that all paths in this route will use.
     * <p>
     * By default, no parent path will be used.
     */
    String path() default "";
}