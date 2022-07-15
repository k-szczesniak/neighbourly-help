package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container;

import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Log
public class PostgresqlExtension implements BeforeAllCallback, AfterAllCallback {

    public static PostgresqlContainer postgresqlContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        postgresqlContainer = new PostgresqlContainer();
        log.info("Starting PostgreSQL Container");
        postgresqlContainer.start();

//        System.setProperty("MONGO_DB_HOST", postgresqlContainer.getContainerIpAddress()); todo: deprcated
//        System.setProperty("MONGO_DB_HOST", postgresqlContainer.getHost()); // TODO: 15.07.2022 zmienić to -> są to odniesienia do application.properties
//        System.setProperty("MONGO_DB_PORT", String.valueOf(postgresqlContainer.getPort()));
//        System.setProperty("MONGO_DB_NAME", "MDB");

        log.info("Finished starting PostgreSQL Container");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info("Stopping PostgreSQL Container");
        postgresqlContainer.stop();
//        System.clearProperty("MONGO_DB_HOST");
//        System.clearProperty("MONGO_DB_PORT");
//        System.clearProperty("MONGO_DB_NAME");
        log.info("Finished stopping PostgreSQL Container");
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);

    }
}