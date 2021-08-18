![Astro](https://raw.githubusercontent.com/Rainnny7/Astro/master/assets/logo.jpg)
# Astro
Astro is a lightweight and easy-to-use RESTful library.

## Compiling
- Clone the repository
- Open the project in your IDE of choice
- Run `mvn clean install`

## Example Usage
```java
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

public final class PersonRoute {
    /**
     * Return a new {@link Person} object based on the provided name in the request.
     *
     * @param request the request
     * @param response the response
     * @return the person
     * @throws RestPathException if the request does not contain a name
     */
    @RestPath(path = "/person", method = RequestMethod.GET)
    public Person getPerson(Request request, Response response) {
        if (request.parameterCount() < 1) {
            response.responseCode(ResponseCode.BAD_REQUEST);
            throw new RestPathException("Missing person name");
        }
        // We do not need to provide a response code here as the default
        // one is 200 (OK).
        return new Person(request.getParameter("name"));
    }
}

@AllArgsConstructor @Getter
public class Person {
    private final String name;
}
```

## License
[MIT](https://choosealicense.com/licenses/mit/)