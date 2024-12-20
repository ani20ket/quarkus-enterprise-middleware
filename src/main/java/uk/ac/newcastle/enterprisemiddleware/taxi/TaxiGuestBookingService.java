package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * <p>Clientside representation of an TaxiBooking object pulled from an external RESTFul API.</p>
 *
 * <p>This is the mirror opposite of a server side JAX-RS service</p>
 *
 */
@Path("/guest-booking")
@RegisterRestClient(configKey = "taxiBooking-api")
public interface TaxiGuestBookingService {

    @POST
    TaxiBooking bookTaxi(@Parameter TaxiGuestBookingRequest taxiGuestBookingRequest);


}