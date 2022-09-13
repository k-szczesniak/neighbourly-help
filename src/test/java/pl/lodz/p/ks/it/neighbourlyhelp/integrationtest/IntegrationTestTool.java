package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.LoginCredentials;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AuthTokenResponseDto;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.ADVERT_ENDPOINT;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.AUTH_ENDPOINT;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.CONTRACT_ENDPOINT;

@ActiveProfiles("test")
@NoArgsConstructor
public class IntegrationTestTool {

    private int port;

    public IntegrationTestTool(int port) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = port;
    }

    public Header generateJwt(String email) {
        String PASSWORD = "zaq1@WSX";

        LoginCredentials body = LoginCredentials.builder()
                .email(email)
                .password(PASSWORD)
                .build();

        final RequestSpecification requestSpecification = given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body);

        final Response response = requestSpecification
                .when()
                .post(AUTH_ENDPOINT.build());

        final AuthTokenResponseDto authTokenResponseDto = response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_JSON_VALUE)
                .extract().response().as(AuthTokenResponseDto.class);

        return new Header("Authorization", String.format("Bearer %s", authTokenResponseDto.getAccessToken()));
    }

    public Header getContractEtag(Long contractId) {

        final RequestSpecification requestSpecification = given()
                .contentType(APPLICATION_JSON_VALUE)
                .header(generateJwt("klient1@klient.pl"));

        final Response response = requestSpecification
                .when()
                .get(String.format("%s/%d", CONTRACT_ENDPOINT.build(), contractId));

        final String etag = response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_JSON_VALUE)
                .extract().header("etag");

        return new Header("If-Match", String.format("%s", etag));
    }

    public Header getAdvertEtag(Long advertId) {

        final RequestSpecification requestSpecification = given()
                .contentType(APPLICATION_JSON_VALUE)
                .header(generateJwt("klient1@klient.pl"));

        final Response response = requestSpecification
                .when()
                .get(String.format("%s/%d", ADVERT_ENDPOINT.build(), advertId));

        final String etag = response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_JSON_VALUE)
                .extract().header("etag");

        return new Header("If-Match", String.format("%s", etag));
    }
}