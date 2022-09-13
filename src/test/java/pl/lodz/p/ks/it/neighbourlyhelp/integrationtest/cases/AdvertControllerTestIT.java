package pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.cases;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.Advert;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.domain.LoyaltyPoint;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.EditAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.dto.request.NewAdvertRequestDto;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.AdvertRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.advertmodule.repository.LoyaltyPointRepository;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.IntegrationTestTool;
import pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.infrastructure.annotation.IntegrationTest;

import java.math.BigInteger;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.lodz.p.ks.it.neighbourlyhelp.integrationtest.BaseIT.Resources.ADVERT_ENDPOINT;

@ActiveProfiles("test")
@IntegrationTest
public class AdvertControllerTestIT extends BaseIT {

    @Autowired
    private IntegrationTestTool integrationTestTool;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private LoyaltyPointRepository loyaltyPointRepository;

    @Test
    public void shouldCreateAdvertSuccessfully() {

        BigInteger advertPrize = BigInteger.valueOf(5);

        Long publisherLPId = -3L;

        LoyaltyPoint publisherLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        int beforeCreateSize = advertRepository.findAll().size();

        // given
        NewAdvertRequestDto requestDto = new NewAdvertRequestDto(
                "Test advert", "Description the same as title", "GARDEN",
                -1L, advertPrize);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .post(ADVERT_ENDPOINT.build());

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        int afterCreateSize = advertRepository.findAll().size();
        assertEquals(beforeCreateSize + 1, afterCreateSize);

        LoyaltyPoint publisherNewLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        assertEquals(publisherLoyaltyPoint.getTotalPoints().subtract(advertPrize), publisherNewLoyaltyPoint.getTotalPoints());
        assertEquals(publisherLoyaltyPoint.getBlockedPoints().add(advertPrize), publisherNewLoyaltyPoint.getBlockedPoints());

    }

    @Test
    public void shouldFailToCreateAdvertWithToHighPrize() {

        BigInteger advertPrize = BigInteger.valueOf(100);

        Long publisherLPId = -3L;

        LoyaltyPoint publisherLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        int beforeCreateSize = advertRepository.findAll().size();

        // given
        NewAdvertRequestDto requestDto = new NewAdvertRequestDto(
                "Test advert", "Description the same as title", "GARDEN",
                -1L, advertPrize);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .post(ADVERT_ENDPOINT.build());

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("exception.loyalty_point.loyalty_points_account_balance_exceeded"));

        int afterCreateSize = advertRepository.findAll().size();
        assertEquals(beforeCreateSize, afterCreateSize);

        LoyaltyPoint publisherNewLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        assertEquals(publisherLoyaltyPoint.getTotalPoints(), publisherNewLoyaltyPoint.getTotalPoints());
        assertEquals(publisherLoyaltyPoint.getBlockedPoints(), publisherNewLoyaltyPoint.getBlockedPoints());

    }

    @ParameterizedTest(name = "[{index}]Edit advert: newAdvertPrize={0}, expectedBlocked={1}, expectedTotal={2}")
    @MethodSource("newAmounts")
    public void shouldEditAdvertSuccessfully(BigInteger newAdvertPrize, BigInteger expectedBlocked, BigInteger expectedTotal, int statusCode) {

        Long advertId = -11L;
        Long publisherLPId = -3L;

        int beforeEditSize = advertRepository.findAll().size();

        // given
        EditAdvertRequestDto requestDto = EditAdvertRequestDto.builder()
                .id(advertId)
                .title("Test advert modified")
                .description("Modified the same as title")
                .category("HOUSEWORK")
                .prize(newAdvertPrize)
                .cityId(-1L)
                .build();

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getAdvertEtag(advertId))
                .body(requestDto);

        // when
        final Response response = requestSpecification
                .when()
                .put(ADVERT_ENDPOINT.build());

        // then
        response
                .then()
                .log().all()
                .statusCode(statusCode);

        int afterEditSize = advertRepository.findAll().size();
        assertEquals(beforeEditSize, afterEditSize);

        LoyaltyPoint publisherNewLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        assertEquals(expectedTotal, publisherNewLoyaltyPoint.getTotalPoints());
        assertEquals(expectedBlocked, publisherNewLoyaltyPoint.getBlockedPoints());

    }

    private static Stream<Arguments> newAmounts() {
        BigInteger advertPrize = BigInteger.valueOf(5);
        return Stream.of(
                arguments(advertPrize.subtract(BigInteger.TWO), BigInteger.valueOf(3), BigInteger.valueOf(17), 200),
                arguments(advertPrize.add(BigInteger.TWO), BigInteger.valueOf(7), BigInteger.valueOf(13), 200),
                arguments(advertPrize, BigInteger.valueOf(5), BigInteger.valueOf(15), 200),
                arguments(advertPrize.add(BigInteger.valueOf(100)), BigInteger.valueOf(5), BigInteger.valueOf(15), 400)
        );
    }

    @Test
    public void shouldDeleteAdvertSuccessfully() {

        Long advertId = -11L;

        BigInteger advertPrize = BigInteger.valueOf(5);

        Long publisherLPId = -3L;

        LoyaltyPoint publisherLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        int beforeDeleteSize = advertRepository.findAll().size();

        // given
        NewAdvertRequestDto requestDto = new NewAdvertRequestDto(
                "Test advert", "Description the same as title", "GARDEN",
                -1L, advertPrize);

        final RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(integrationTestTool.generateJwt("klient1@klient.pl"))
                .header(integrationTestTool.getAdvertEtag(advertId));

        // when
        final Response response = requestSpecification
                .when()
                .delete(String.format("%s/%d", ADVERT_ENDPOINT.build(), advertId));

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        int afterDeleteSize = advertRepository.findAll().size();
        assertEquals(beforeDeleteSize - 1, afterDeleteSize);

        LoyaltyPoint publisherNewLoyaltyPoint = loyaltyPointRepository.findById(publisherLPId).get();

        assertEquals(publisherLoyaltyPoint.getTotalPoints().add(advertPrize), publisherNewLoyaltyPoint.getTotalPoints());
        assertEquals(publisherLoyaltyPoint.getBlockedPoints().subtract(advertPrize), publisherNewLoyaltyPoint.getBlockedPoints());

    }

    @Test
    public void shouldApproveAdvertSuccessfully() {

        Long advertId = -6L;
        Header jwt = integrationTestTool.generateJwt("adam@amc.pl");

        //approve
        // given
        final RequestSpecification requestApproveSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(jwt)
                .header(integrationTestTool.getAdvertEtag(advertId));

        // when
        final Response responseApprove = requestApproveSpecification
                .when()
                .patch(String.format("%s/approve/%d", ADVERT_ENDPOINT.build(), advertId));

        // then
        responseApprove
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Advert approvedAdvert = advertRepository.findById(advertId).get();
        assertTrue(approvedAdvert.isApproved());
    }

    @Test
    public void shouldDisapproveAdvertSuccessfully() {

        Long advertId = -11L;
        Header jwt = integrationTestTool.generateJwt("adam@amc.pl");

        //disapprove
        // given
        final RequestSpecification requestDisapproveSpecification = given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .header(jwt)
                .header(integrationTestTool.getAdvertEtag(advertId));

        // when
        final Response responseDisapprove = requestDisapproveSpecification
                .when()
                .patch(String.format("%s/disapprove/%d", ADVERT_ENDPOINT.build(), advertId));

        // then
        responseDisapprove
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());

        Advert disapproveAdvert = advertRepository.findById(advertId).get();
        assertFalse(disapproveAdvert.isApproved());
    }

}