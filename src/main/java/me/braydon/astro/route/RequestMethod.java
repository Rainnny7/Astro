package me.braydon.astro.route;

/**
 * This represents an HTTP request method.
 *
 * @author Braydon
 */
public enum RequestMethod {
    /**
     * The GET method requests a representation of the specified resource. Requests using GET should only retrieve data.
     */
    GET,

    /**
     * The POST method is used to submit an entity to the specified resource, often causing a change in state or side effects on the server.
     */
    POST,

    /**
     * The PUT method replaces all current representations of the target resource with the request payload.
     */
    PUT,

    /**
     * The PATCH method is used to apply partial modifications to a resource.
     */
    PATCH,

    /**
     * The DELETE method deletes the specified resource.
     */
    DELETE,

    /**
     * Describes the communication options for the target resource.
     */
    OPTIONS;

    /**
     * Get the request method by the given name.
     *
     * @param methodName the name of the request method
     * @return the request method, null if none
     */
    public static RequestMethod byName(String methodName) {
        for (RequestMethod method : values()) {
            if (method.name().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}