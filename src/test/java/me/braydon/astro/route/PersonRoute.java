package me.braydon.astro.route;

import me.braydon.astro.exception.RestPathException;
import me.braydon.astro.modal.Person;
import me.braydon.astro.mysql.MySQLConnector;
import me.braydon.astro.repository.PersonRepository;

import java.util.List;

/**
 * @author Braydon
 */
@Route(path = "/people")
public final class PersonRoute {
    // Constructing the person repository and adding it
    private final PersonRepository repository = MySQLConnector.addRepository(new PersonRepository());

    /**
     * Return a list of people that have the same name as the one in
     * the request parameter.
     *
     * @param request the request
     * @param response the response
     * @return the people
     * @see Person for people
     * @throws RestPathException if the request does not contain a name
     */
    @RestPath(path = "/filter", method = RequestMethod.GET)
    public List<Person> getPeopleFiltered(Request request, Response response) {
        if (request.parameterCount() < 1) {
            response.responseCode(ResponseCode.BAD_REQUEST);
            throw new RestPathException("Missing person name");
        }
        // We do not need to provide a response code here as the default
        // one is 200 (OK).
        return repository.byFirstName(request.getParameter("name"));
    }

    /**
     * Return a list of people.
     *
     * @param request the request
     * @param response the response
     * @return the people
     * @see Person for people
     * @throws RestPathException if the request does not contain a name
     */
    @RestPath(path = "/all", method = RequestMethod.GET)
    public List<Person> getPeople(Request request, Response response) {
        // We do not need to provide a response code here as the default
        // one is 200 (OK).
        return repository.findAll();
    }
}