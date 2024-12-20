package uk.ac.newcastle.enterprisemiddleware.flight;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * REST client interface for interacting with the Flight API. This interface provides
 * methods to retrieve flight details, such as fetching all available flights.
 */
@Path("/flights")
@RegisterRestClient(configKey = "flightBooking-api")
public interface FlightService {

    /**
     * Retrieves a list of all available flights.
     *
     * @return a list of all available flights
     */
    @GET
    List<Flight> getAllFlights();

}