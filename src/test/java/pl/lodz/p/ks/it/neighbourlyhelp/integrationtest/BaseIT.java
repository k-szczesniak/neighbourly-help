package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.logging.Level;
import java.util.logging.LogManager;

//@DirtiesContext
//public class BaseIT {
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.basePath = "/api";
//        RestAssured.port = this.port;
//    }
//
//    public enum Resources {
//        AUTH_ENDPOINT("/auth/login");
//
//        private final String endpoint;
//
//        Resources(String endpoint) {
//            this.endpoint = endpoint;
//        }
//
//        public String build() {
//            return endpoint;
//        }
//    }
//}

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public  class BaseIT {

    static {
        // Postgres JDBC driver uses JUL; disable it to avoid annoying, irrelevant, stderr logs during connection testing
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
    }

    public static PostgreSQLContainer<?> postgresDB;

    static {
        postgresDB = new PostgreSQLContainer<>("postgres:13.4")
                .withDatabaseName("eis")
                        .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));
//                .withStartupCheckStrategy(new LogMessageWaitStrategy().withRegEx(".*database system is ready to accept connections.*\\s")
//                        .withTimes(2)
//                        .withStartupTimeout(Duration.of(60, SECONDS)));


        postgresDB.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = this.port;
    }
}