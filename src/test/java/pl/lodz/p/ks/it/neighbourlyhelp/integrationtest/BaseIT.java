package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
public class BaseIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = this.port;
    }

    enum Resources {
        V1_CUSTOMERS_ENDPOINT("/v1/customers");

        private final String endpoint;

        Resources(String endpoint) {
            this.endpoint = endpoint;
        }

        public String build() {
            return endpoint;
        }
    }
}