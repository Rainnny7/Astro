package me.braydon.astro.mysql;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.SneakyThrows;
import me.braydon.astro.modal.Modal;
import me.braydon.astro.mysql.column.Column;
import me.braydon.astro.mysql.column.ColumnFilter;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Braydon
 */
public abstract class MySQLRepository<T extends Modal> {
    private final Class<? extends T> clazz;
    @Setter(AccessLevel.PROTECTED) protected MySQLConnector mySQLConnector;

    public MySQLRepository(Class<? extends T> clazz) {
        this.clazz = clazz;
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Provided modal does not have the @Table annotation");
        }
    }

    /**
     * Get a {@link Modal} from the table with optional filters.
     *
     * @param filters the optional filters to use
     * @return the filtered modal.
     * @see ColumnFilter for filter
     */
    public final T find(ColumnFilter... filters) {
        List<T> modals = findAll(filters);
        return modals.isEmpty() ? null : modals.get(0);
    }

    /**
     * Get a list of {@link Modal}'s from the table with optional filters.
     *
     * @param filters the optional filters to use
     * @return the filtered modals.
     * @see ColumnFilter for filter
     */
    @SneakyThrows
    public final List<T> findAll(ColumnFilter... filters) {
        Table table = clazz.getAnnotation(Table.class);

        // Constructing the query string to execute
        StringBuilder queryStringBuilder = new StringBuilder("SELECT * FROM `" + table.name() + "`" + (filters.length > 0 ? " WHERE " : ""));
        for (int i = 0; i < filters.length; i++) {
            ColumnFilter filter = filters[i];
            queryStringBuilder.append(i > 0 ? " AND " : "").append("`").append(filter.getColumn()).append("` = '")
                    .append(filter.getValue().toString()).append("'");
        }
        queryStringBuilder.append(";");

        List<T> modals = new ArrayList<>();
        // Opening the connection to MySQL
        try (Connection connection = mySQLConnector.getDataSource().getConnection()) {
            // Executing the built query
            ResultSet resultSet = connection.prepareStatement(queryStringBuilder.toString()).executeQuery();

            while (resultSet.next()) {
                T instance = (T) clazz.newInstance(); // Construct a new object of the given generic type for each row
                for (Field field : clazz.getDeclaredFields()) { // Loop over the fields in the modal
                    // If the looped-field does not annotate @Column, continue
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }
                    Column column = field.getAnnotation(Column.class);
                    String columnName = column.name();
                    if (columnName.isEmpty()) { // If the annotation does not contain a column name, use the field name
                        columnName = field.getName();
                    }
                    boolean previousAccessibility = field.isAccessible();
                    field.setAccessible(true); // Making the field accessible so it can be modified
                    field.set(instance, resultSet.getObject(columnName, column.type().getType())); // Setting the field
                    field.setAccessible(previousAccessibility); // Resetting the field accessibility
                }
                modals.add(instance); // Adding the modal to the list
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return modals;
    }
}