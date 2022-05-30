# ![Astro](assets/logo.jpg) Astro
Astro is a lightweight and easy-to-use RESTful library.

### Note
This project is no-longer maintained, use at your own risk.

## Compiling
- Clone the repository
- Open the project in your IDE of choice
- Run `mvn clean install`

## Dependency
### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Rainnny7</groupId>
    <artifactId>Astro</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.Rainnny7:Astro:VERSION'
}
```

## Example Usage
```java
public class AstroExample {
    public static void main(String[] args) {
        // Starts the Astro server on port 8080 with the PersonRoute
        Astro astro = Astro.builder(8080)
                .withMySQLConnector(new MySQLConnector(new HikariConfig() {{ // This is only needed if you plan to use MySQL
                    setJdbcUrl("jdbc:mysql://localhost:3306/astro");
                    setUsername("astro");
                    setPassword("p4$$w0rd");
                }}))
                // Adding the person route - It's important that if you're using MySQL
                // that routes are added after setting the MySQL connector
                .addRoute(new PersonRoute())
                .build().start();

        // Adds a shutdown hook - This ensures that the Astro server shuts down correctly
        // when the application exits.
        Runtime.getRuntime().addShutdownHook(new Thread(astro::cleanup));
    }
}

public final class PersonRoute {
    // Constructing the person repository and adding it
    private final PersonRepository repository = MySQLConnector.addRepository(new PersonRepository());

    /**
     * Return a list of people that have the same name as the one in
     * the request parameter.
     *
     * @param request the request
     * @param response the response
     * @return the person
     * @see Person for people
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

public final class PersonRepository extends MySQLRepository<Person> {
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

@Table(name = "people") @Getter
public class Person extends Modal {
    @Column(type = ColumnType.INT) private long id;
    @Column(type = ColumnType.VARCHAR) private String firstName, lastName;
    @Column(type = ColumnType.INT) private int age;
}
```

## TODO
- [x] MySQL Support
- [x] Event System
- [ ] Add @NonNull annotations to make the code more understandable
- [ ] Custom 404 Pages
- [ ] Static Pages
- [ ] Multipart File Uploader

## License
[MIT](https://choosealicense.com/licenses/mit/)