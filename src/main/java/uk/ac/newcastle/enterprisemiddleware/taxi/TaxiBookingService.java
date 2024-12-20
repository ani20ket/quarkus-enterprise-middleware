package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * <p>Clientside representation of an TaxiBooking object pulled from an external RESTFul API.</p>
 *
 * <p>This is the mirror opposite of a server side JAX-RS service</p>
 *
 */
@Path("/bookings")
@RegisterRestClient(configKey = "taxiBooking-api")
public interface TaxiBookingService {

    @DELETE
    @Path("/{id:[0-9]+}")
    void deleteTaxi(@PathParam("id") Long id);


}