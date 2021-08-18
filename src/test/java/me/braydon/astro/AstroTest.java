package me.braydon.astro;

import com.zaxxer.hikari.HikariConfig;
import me.braydon.astro.mysql.MySQLConnector;
import me.braydon.astro.route.PersonRoute;
import org.junit.jupiter.api.Test;

/**
 * @author Braydon
 */
public class AstroTest {
    private static final long MAX_ALIVE_TIME = 10000L;

    @Test
    public void runTest() {
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

        // We keep the test running for a predefined amount of time so the test doesn't
        // finish instantly
        long started = System.currentTimeMillis();
        while ((System.currentTimeMillis() - started) < MAX_ALIVE_TIME) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}