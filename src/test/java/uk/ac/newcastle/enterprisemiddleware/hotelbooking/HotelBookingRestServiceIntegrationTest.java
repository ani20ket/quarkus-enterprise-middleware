package uk.ac.newcastle.enterprisemiddleware.hotelbooking;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestHTTPEndpoint(HotelBookingRestService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class HotelBookingRestServiceIntegrationTest {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService customerService;

    @Inject
    HotelService hotelService;

    private static HotelBookingDTO hotelBookingDTO;
    private static HotelBooking hotelBooking = new HotelBooking();
    static Customer customer = new Customer();
    private static Hotel hotel = new Hotel();

    @BeforeEach
    @Transactional
    void setup() throws Exception {
        hotelBookingDTO = new HotelBookingDTO();
        hotelBookingDTO.setHotelId("1");
        hotelBookingDTO.setCustomerId("1");
        hotelBookingDTO.setCheckInDate(LocalDate.now().plusDays(8));
        hotelBookingDTO.setCheckOutDate(LocalDate.now().plusDays(9));
        hotelBookingDTO.setNoOfGuests(4L);
        hotelBookingDTO.setNoOfRooms(3L);

        hotel.setHotelName("Hotel");
        hotel.setPostCode("NE12QF");
        hotel.setPhoneNumber("01234567890");

        hotelBooking.setId(1L);
        hotelBooking.setHotelId("1");
        hotelBooking.setCheckInDate(LocalDate.now().plusDays(1));
        hotelBooking.setCheckOutDate(LocalDate.now().plusDays(4));
        hotelBooking.setNoOfGuests(4L);
        hotelBooking.setNoOfRooms(3L);


        customer.setName("TestAccount");
        customer.setEmail("test@email.com");
        customer.setPhoneNumber("01234567891");

    }

    @Test
    @Order(1)
    @Transactional
    public void testCanCreateHotel() throws Exception {
        customerService.create(customer);
        hotelService.create(hotel);
        given().
                contentType(ContentType.JSON).
                body(hotelBookingDTO).
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

        HotelBooking[] result = response.body().as(HotelBooking[].class);

        System.out.println(result[0]);

        assertEquals(1, result.length);
        assertTrue(hotelBooking.getHotelId().equals(result[0].getHotelId()), "hotel id not equal");
        assertTrue(hotelBooking.getCheckInDate().equals(result[0].getCheckInDate()), "checkInDate not equal");
        assertTrue(hotelBooking.getCheckOutDate().equals(result[0].getCheckOutDate()), "checkOutDate not equal");
        assertTrue(hotelBooking.getNoOfGuests() == result[0].getNoOfGuests(), "noOfGuests not equal");
        assertTrue(hotelBooking.getNoOfRooms() == result[0].getNoOfRooms(), "noOfRooms not equal");
    }

    @Test
    @Order(3)
    public void testDuplicateDateCausesError() {
        given().
                contentType(ContentType.JSON).
                body(hotelBookingDTO).
        when().
                post().
        then().
                statusCode(409);
    }

    @Test
    @Order(4)
    public void testCanDeleteHotel() {
        Response response = when().
                get().
                then().
                statusCode(200).
                extract().response();

        HotelBooking[] result = response.body().as(HotelBooking[].class);

        when().
                delete(result[0].getId().toString()).
        then().
                statusCode(204);
    }
}