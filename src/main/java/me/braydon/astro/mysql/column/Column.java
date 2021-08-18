package me.braydon.astro.mysql.column;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation represents column in MySQL.
 *
 * @author Braydon
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * Get the name of the column.
     * <p>
     * The name of the field this column belongs to is used by default.
     */
    String name() default "";

    /**
     * Get the type of this column.
     */
    ColumnType type();
}