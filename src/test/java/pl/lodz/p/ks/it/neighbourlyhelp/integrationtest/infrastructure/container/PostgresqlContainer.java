package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.container;

import org.testcontainers.containers.PostgreSQLContainer;

//public class PostgresqlContainer extends GenericContainer<PostgresqlContainer> {
//
//    public static final String DEFAULT_IMAGE_AND_TAG = "postgres:13.4";
//    public static final int DEFAULT_PORT = 27017;
//
//    public PostgresqlContainer() {
//        super(DEFAULT_IMAGE_AND_TAG);
//        withNetwork(NETWORK).withExposedPorts(DEFAULT_PORT);
//    }
//
////    @NotNull
//    public Integer getPort() {
//        return getMappedPort(DEFAULT_PORT);
//    }
//}

//@Testcontainers
//public class PostgresqlContainer {
//
//    public static PostgreSQLContainer<?> postgresDB;
//
//    static {
//        postgresDB = new PostgreSQLContainer<>("postgres:13.2")
//                .withDatabaseName("eis");
//
////        postgresDB.start();
//    }
//}

public class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {

    public static final String DEFAULT_IMAGE_AND_TAG = "postgres:13.4";
    public static final String CONTAINER_NAME = "postgresqltest";

    public PostgresqlContainer() {
        super(CONTAINER_NAME);
        withDatabaseName("test");
        waitUntilContainerStarted();
//        withNetwork(NETWORK).withExposedPorts(DEFAULT_PORT);
    }

}