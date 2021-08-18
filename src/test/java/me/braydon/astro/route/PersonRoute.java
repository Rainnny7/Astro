package me.braydon.astro.route;

import me.braydon.astro.exception.RestPathException;
import me.braydon.astro.modal.Person;
import me.braydon.astro.mysql.MySQLConnector;
import me.braydon.astro.repository.PersonRepository;

import java.util.List;

/**
 * @author Braydon
 */
public final class PersonRoute {
    // Constructing the person repository and adding it
    private final PersonRepository repository = MySQLConnector.addRepository(new PersonRepository());

    /**
     * Return a new {@link Person} object based on the provided name in the request.
     *
     * @param request the request
     * @param response the response
     * @return the person
     * @throws RestPathException if the request does not contain a name
     */
    @RestPath(path = "/person", method = RequestMethod.GET)
    public List<Person> getPerson(Request request, Response response) {
        if (request.parameterCount() < 1) {
            response.responseCode(ResponseCode.BAD_REQUEST);
            throw new RestPathException("Missing person name");
        }
        // We do not need to provide a response code here as the default
        // one is 200 (OK).
        return repository.byFirstName(request.getParameter("name"));
    }
}