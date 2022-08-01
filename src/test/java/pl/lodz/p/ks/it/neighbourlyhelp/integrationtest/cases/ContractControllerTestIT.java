package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.cases;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.ContractRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.IntegrationTestTool;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.annotation.IntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.CONTRACT_ENDPOINT;

@ActiveProfiles("test")
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/test-init.sql")
public class ContractControllerTestIT extends BaseIT {

    @Autowired
    private IntegrationTestTool integrationTestTool;

    @Autowired
    private ContractRepository contractRepository;

    @Test
    public void shouldCreateContractSuccessfully() {

        int beforeCreateSize = contractRepository.findAll().size();

        // given
        NewContractRequestDto requestDto = new NewContractRequestDto(-5L);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .post(CONTRACT_ENDPOINT.build());

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        int afterCreateSize = contractRepository.findAll().size();

        assertEquals(beforeCreateSize + 1, afterCreateSize);
    }

    @Test
    public void shouldFailWithDisapprovedContract() {

        int beforeCreateSize = contractRepository.findAll().size();

        // given
        NewContractRequestDto requestDto = new NewContractRequestDto(-6L);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .post(CONTRACT_ENDPOINT.build());

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("exception.contract.advert_is_disapproved"));

        int afterCreateSize = contractRepository.findAll().size();

        assertEquals(beforeCreateSize, afterCreateSize);
    }

}