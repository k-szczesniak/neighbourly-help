package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.cases;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.request.RegisterAccountDto;
import pl.lodz.p.ks.it.neighbourlyhelp.clientmodule.dto.response.AuthTokenResponseDto;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.AUTH_ENDPOINT;

@Log
@ActiveProfiles("test")
//@IntegrationTest
public class ExampleTestIT extends BaseIT {

    @Test
    public void shouldAuthenticateCorrectly() {
        // given
//        Customer customer = createCustomer();

        JSONObject body = new JSONObject();
        try {
            body.put("email", "adam@amc.pl");
            body.put("password", "zaq1@WSX");
        } catch (JSONException e) {
            log.info("problem with parsing");
        }


//        String body = {
//                "email": "adam@amc.pl",
//                "password": "zaq1@WSX"
//}

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body);

        // when
        final Response response = requestSpecification
                .when()
//                .post(AUTH_ENDPOINT.build());
                .post("/auth/login");

        // then
        final AuthTokenResponseDto authTokenResponseDto = response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(APPLICATION_JSON_VALUE)
                .extract().response().as(AuthTokenResponseDto.class);

        assertThat(authTokenResponseDto.getAccessToken()).isNotEmpty();
        assertThat(authTokenResponseDto.getRefreshToken()).isNotEmpty();
//        assertThat(customerResponse.getFirstName()).isEqualTo(customer.getFirstName());
//        assertThat(customerResponse.getLastName()).isEqualTo(customer.getLastName());
//        assertThat(customerResponse.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    public void shouldReturnRandom() {

        final RequestSpecification requestSpecification = given()
                .log().all();

        // when
        final Response response = requestSpecification
                .when()
                .get("/api/rand");

        // then
        Long randNumber = Long.valueOf(response.getBody().asString());

        assertThat(randNumber).isPositive();

    }

    @Test
    public void shouldRegisterAccount() {
        // given
        RegisterAccountDto registerAccountDto = new RegisterAccountDto("Filip", "Kowalski", "filip@kowalski.pl", "zaq1@WSX", "123321123");

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registerAccountDto);

        // when
        final Response response = requestSpecification
                .when()
                .post("/account/register");

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }
}