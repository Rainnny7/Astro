package me.braydon.astro.repository;

import me.braydon.astro.modal.Person;
import me.braydon.astro.mysql.MySQLRepository;
import me.braydon.astro.mysql.column.ColumnFilter;

import java.util.List;

/**
 * @author Braydon
 */
public class PersonRepository extends MySQLRepository<Person> {
    public PersonRepository() {
        super(Person.class);
    }

    /**
     * Get a list of people by their first name.
     *
     * @param firstName the first name
     * @return the list
     * @see Person for people
     */
    public List<Person> byFirstName(String firstName) {
        return filter(new ColumnFilter("firstName", firstName));
    }
}