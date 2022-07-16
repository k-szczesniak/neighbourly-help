package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.logging.Level;
import java.util.logging.LogManager;

@Slf4j
public class PostgresqlExtension implements BeforeAllCallback, AfterAllCallback {

    static {
        // Postgres JDBC driver uses JUL; disable it to avoid annoying, irrelevant, stderr logs during connection testing
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
    }

    public static PostgreSQLContainer<?> postgresDB;

    static {
        postgresDB = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("test")
                .withLogConsumer(new Slf4jLogConsumer(log))
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));
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