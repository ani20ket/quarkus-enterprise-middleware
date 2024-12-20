package uk.ac.newcastle.enterprisemiddleware.flight;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


/**
 * REST client interface for interacting with the Flight Booking API. This interface defines
 * methods for managing flight bookings, such as deleting a booking.
 */
@Path("/bookings")
@RegisterRestClient(configKey = "flightBooking-api")
public interface FlightBookingService {

    /**
     * Deletes a flight booking identified by the given ID.
     *
     * @param id the ID of the flight booking to be deleted
     */
    @DELETE
    @Path("{id:[0-9]+}")
    void deleteBooking(@PathParam("id") Long id);

}