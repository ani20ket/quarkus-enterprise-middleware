package uk.ac.newcastle.enterprisemiddleware.hotel;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(HotelRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class HotelRestServiceIntegrationTest {

    @Inject
    @Named("logger")
    Logger log;

    private static Hotel hotel;

    @BeforeEach
    void setup() {
        log.info("Running setup");
        hotel = new Hotel();
        hotel.setHotelName("TestHotel");
        hotel.setPhoneNumber("01234567890");
        hotel.setPostCode("NE12QF");
    }

    @Test
    @Order(1)
    public void testCanCreateHotel() {
        given().
                contentType(ContentType.JSON).
                body(hotel).
        when()
                .post().
        then().
                statusCode(201);
    }

    @Test
    @Order(2)
    public void testCanGetHotels() {
        Response response = when().
                get().
        then().
                statusCode(200).
                extract().response();

        Hotel[] result = response.body().as(Hotel[].class);

        System.out.println(result[0]);

        assertEquals(1, result.length);
        assertTrue(hotel.getHotelName().equals(result[0].getHotelName()), "Name not equal");
        assertTrue(hotel.getPhoneNumber().equals(result[0].getPhoneNumber()), "Phone number not equal");
        assertTrue(hotel.getPostCode().equals(result[0].getPostCode()), "PostCode not equal");
    }

    @Test
    @Order(3)
    public void testDuplicateEmailCausesError() {
        given().
                contentType(ContentType.JSON).
                body(hotel).
        when().
                post().
        then().
                statusCode(409).
                body("reasons.email", containsString("phone is already used"));
    }

    @Test
    @Order(4)
    public void testCanDeleteHotel() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        Hotel[] result = response.body().as(Hotel[].class);

        when().
                delete(result[0].getId().toString()).
        then().
                statusCode(204);
    }
}