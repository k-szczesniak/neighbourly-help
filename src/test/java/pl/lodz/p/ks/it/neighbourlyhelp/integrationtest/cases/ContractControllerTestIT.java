package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.cases;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Contract;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.LoyaltyPoint;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.enums.ContractStatus;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.ApproveFinishedRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewContractRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.ContractRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.LoyaltyPointRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.IntegrationTestTool;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.annotation.IntegrationTest;

import java.math.BigInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.CONTRACT_ENDPOINT;

@ActiveProfiles("test")
@IntegrationTest
public class ContractControllerTestIT extends BaseIT {

    @Autowired
    private IntegrationTestTool integrationTestTool;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

    @Test
    public void shouldCreateContractSuccessfully() {

        int beforeCreateSize = contractRepository.findAll().size();

        // given
        NewContractRequestDto requestDto = new NewContractRequestDto(-5L);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getAdvertEtag(-5L))
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
                .header(integrationTestTool.getAdvertEtag(-6L))
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

    @Test
    public void shouldFailForConcurrentCreateContract() {

        int beforeCreateSize = contractRepository.findAll().size();

        // given
        NewContractRequestDto requestDto = new NewContractRequestDto(-7L);

        Header jwtKlient1 = integrationTestTool.generateJwt("klient1@klient.pl");
        Header jwtKlient2 = integrationTestTool.generateJwt("klient2@klient.pl");

        final RequestSpecification requestSpecificationKlient1 = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(jwtKlient1)
                .header(integrationTestTool.getAdvertEtag(-7L))
                .body(requestDto);

        final RequestSpecification requestSpecificationKlient2 = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(jwtKlient2)
                .header(integrationTestTool.getAdvertEtag(-7L))
                .body(requestDto);

        // when
        final Response responseKlient1 = requestSpecificationKlient1
                .when()
                .post(CONTRACT_ENDPOINT.build());

        final Response responseKlient2 = requestSpecificationKlient2
                .when()
                .post(CONTRACT_ENDPOINT.build());

        // then
        responseKlient1
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        responseKlient2
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("exception.app_optimistic_lock.optimistic_lock"));

        int afterCreateSize = contractRepository.findAll().size();

        assertEquals(beforeCreateSize + 1, afterCreateSize);
    }

    @Test
    public void shouldCancelContractSuccessfully() {

        // given
        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(-1L));

        // when
        final Response response = requestSpecification
                .when()
                .patch(String.format("%s/cancel/-1", CONTRACT_ENDPOINT.build()));

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    public void shouldFailCancelInProgressContract() {

        // given
        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(-2L));

        // when
        final Response response = requestSpecification
                .when()
                .patch(String.format("%s/cancel/-2", CONTRACT_ENDPOINT.build()));

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("exception.contract.in_progress_cancel_contract"));

    }

    @Test
    public void shouldStartAndEndContractSuccessfully() {

        Long contractId = -1L;

        //starting contract
        // given
        final RequestSpecification requestStartSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(contractId));

        // when
        final Response responseStart = requestStartSpecification
                .when()
                .patch(String.format("%s/start/%d", CONTRACT_ENDPOINT.build(), contractId));

        // then
        responseStart
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Contract startedContract = contractRepository.findById(contractId).get();
        assertEquals(ContractStatus.IN_PROGRESS, startedContract.getStatus());

        //ending contract
        // given
        final RequestSpecification requestEndSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(contractId));

        // when
        final Response responseEnd = requestEndSpecification
                .when()
                .patch(String.format("%s/end/%d", CONTRACT_ENDPOINT.build(), contractId));

        // then
        responseEnd
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Contract endedContract = contractRepository.findById(contractId).get();
        assertEquals(ContractStatus.TO_APPROVE, endedContract.getStatus());

    }

    @Test
    public void shouldApproveContractTerminationSuccessfully() {

        BigInteger advertPrize = BigInteger.valueOf(5);

        Long publisherLPId = -1L;
        Long executorLPId = -3L;
        Long contractId = -3L;

        LoyaltyPoint publisherLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();
        LoyaltyPoint executorLoyaltyPoint = loyaltyPointRepository.findById(executorLPId).get();

        // given
        ApproveFinishedRequestDto requestDto = new ApproveFinishedRequestDto(contractId);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(contractId))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .patch(String.format("%s/approve", CONTRACT_ENDPOINT.build()));

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Contract approvedContract = contractRepository.findById(contractId).get();

        assertEquals(ContractStatus.FINISHED, approvedContract.getStatus());

        LoyaltyPoint publisherNewLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();
        assertEquals(publisherLoyaltyPoint.getBlockedPoints().subtract(advertPrize), publisherNewLoyaltyPoint.getBlockedPoints());
        assertEquals(publisherLoyaltyPoint.getTotalPoints(), publisherNewLoyaltyPoint.getTotalPoints());

        LoyaltyPoint executorNewLoyaltyPoint = loyaltyPointRepository.findById(executorLPId).get();
        assertEquals(executorLoyaltyPoint.getTotalPoints().add(advertPrize), executorNewLoyaltyPoint.getTotalPoints());
        assertEquals(executorLoyaltyPoint.getBlockedPoints(), executorNewLoyaltyPoint.getBlockedPoints());

    }

    @Test
    public void shouldApproveTwoContractTerminationConnectedWithTheSameExecutorSuccessfully() {

        BigInteger advertPrize = BigInteger.valueOf(5);

        Long executorLPId = -3L; // klient1
        Long publisher1LPId = -1L; //piotrnowak
        Long publisher2LPId = -4L; //klient2
        Long contract1Id = -3L;
        Long contract2Id = -4L;

        LoyaltyPoint publisher1LoyaltyPoint = loyaltyPointRepository.findById(publisher1LPId).get();
        LoyaltyPoint publisher2LoyaltyPoint = loyaltyPointRepository.findById(publisher2LPId).get();
        LoyaltyPoint executorLoyaltyPoint = loyaltyPointRepository.findById(executorLPId).get();

        // given
        ApproveFinishedRequestDto requestC1Dto = new ApproveFinishedRequestDto(contract1Id);
        ApproveFinishedRequestDto requestC2Dto = new ApproveFinishedRequestDto(contract2Id);

        final RequestSpecification requestC1Specification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(contract1Id))
                .body(requestC1Dto);

        final RequestSpecification requestC2Specification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getContractEtag(contract2Id))
                .body(requestC2Dto);

        // when
        final Response responseC1 = requestC1Specification
                .when()
                .patch(String.format("%s/approve", CONTRACT_ENDPOINT.build()));

        final Response responseC2 = requestC2Specification
                .when()
                .patch(String.format("%s/approve", CONTRACT_ENDPOINT.build()));

        // then
        responseC1
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        responseC2
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Contract approvedContract1 = contractRepository.findById(contract1Id).get();
        Contract approvedContract2 = contractRepository.findById(contract2Id).get();

        assertEquals(ContractStatus.FINISHED, approvedContract1.getStatus());
        assertEquals(ContractStatus.FINISHED, approvedContract2.getStatus());

        LoyaltyPoint publisher1NewLoyaltyPoint = loyaltyPointRepository.findById(publisher1LPId).get();
        assertEquals(publisher1LoyaltyPoint.getBlockedPoints().subtract(advertPrize), publisher1NewLoyaltyPoint.getBlockedPoints());
        assertEquals(publisher2LoyaltyPoint.getTotalPoints(), publisher2LoyaltyPoint.getTotalPoints());

        LoyaltyPoint publisher2NewLoyaltyPoint = loyaltyPointRepository.findById(publisher2LPId).get();
        assertEquals(publisher1LoyaltyPoint.getBlockedPoints().subtract(advertPrize), publisher2NewLoyaltyPoint.getBlockedPoints());
        assertEquals(publisher2LoyaltyPoint.getTotalPoints(), publisher2NewLoyaltyPoint.getTotalPoints());

        LoyaltyPoint executorNewLoyaltyPoint = loyaltyPointRepository.findById(executorLPId).get();
        assertEquals(executorLoyaltyPoint.getTotalPoints().add(advertPrize.multiply(BigInteger.TWO)), executorNewLoyaltyPoint.getTotalPoints());
        assertEquals(executorLoyaltyPoint.getBlockedPoints(), executorNewLoyaltyPoint.getBlockedPoints());

    }
}