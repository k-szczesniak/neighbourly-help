package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container;

import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.logging.Level;
import java.util.logging.LogManager;

@Log
public class PostgresqlExtension implements BeforeAllCallback, AfterAllCallback {

    static {
        // Postgres JDBC driver uses JUL; disable it to avoid annoying, irrelevant, stderr logs during connection testing
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
    }

    public static PostgreSQLContainer<?> postgresDB;

    static {
        postgresDB = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("test")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));
//                .withStartupCheckStrategy(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s")
//                        .withTimes(2)
//                        .withStartupTimeout(Duration.of(60, SECONDS)));


//        postgresDB.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }


    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("Starting PostgreSQL Container");

        postgresDB.start();

        log.info("Finished starting PostgreSQL Container");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info("Stopping PostgreSQL Container");

        postgresDB.stop();

        log.info("Finished stopping PostgreSQL Container");
    }
}