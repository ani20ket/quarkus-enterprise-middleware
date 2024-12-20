package uk.ac.newcastle.enterprisemiddleware.flight;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST client interface for interacting with the Flight Booking API. This interface defines
 * methods for managing flight bookings, such as creating a booking.
 */
@Path("/guestbookings")
@RegisterRestClient(configKey = "flightBooking-api")
public interface FlightGuestBookingService {

    /**
     * Creates a booking for a flight
     *
     * @param flightGuestBookingRequest the details of the flight booking to be booked
     */
    @POST
    FlightBooking bookFlight(@Parameter FlightGuestBookingRequest flightGuestBookingRequest);

}