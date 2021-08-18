package me.braydon.astro.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Braydon
 */
@Getter(AccessLevel.PROTECTED)
public class MySQLConnector {
    private static MySQLConnector INSTANCE;
    private static final Set<MySQLRepository<?>> repositories = new HashSet<>();

    private final HikariDataSource dataSource;

    public MySQLConnector(HikariConfig config) {
        this(config, true);
    }

    public MySQLConnector(HikariConfig config, boolean applyDefaultSettings) {
        INSTANCE = this;
        // Apply the default settings if enabled
        if (applyDefaultSettings) {
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }
        dataSource = new HikariDataSource(config);
    }

    /**
     * Add the given repository.
     *
     * @param repository the repository to add
     * @return the added repository
     */
    public static <T extends MySQLRepository<?>> T addRepository(T repository) {
        repository.setMySQLConnector(INSTANCE);
        repositories.add(repository);
        return repository;
    }
}