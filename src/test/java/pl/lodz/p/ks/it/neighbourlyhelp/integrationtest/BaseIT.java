package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.web.server.LocalServerPort;

public class BaseIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = this.port;
    }

    public enum Resources {
        AUTH_ENDPOINT("/auth/login"),
        REGISTRATION_ENDPOINT("/account/register"),
        CONTRACT_ENDPOINT("/contract"),
        ADVERT_ENDPOINT("/advert");

        private final String endpoint;

        Resources(String endpoint) {
            this.endpoint = endpoint;
        }

        public String build() {
            return endpoint;
        }
    }
}