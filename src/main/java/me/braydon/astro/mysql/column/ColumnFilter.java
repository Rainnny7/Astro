package me.braydon.astro.mysql.column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.braydon.astro.mysql.MySQLRepository;

/**
 * This represents a filter that is used in a {@link MySQLRepository}.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter
public class ColumnFilter {
    private final String column;
    private final Object value;
}