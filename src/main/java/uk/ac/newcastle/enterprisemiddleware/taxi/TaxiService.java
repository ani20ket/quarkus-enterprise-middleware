package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * <p>Clientside representation of an Taxi object pulled from an external RESTFul API.</p>
 *
 * <p>This is the mirror opposite of a server side JAX-RS service</p>
 *
 */
@Path("/taxi")
@RegisterRestClient(configKey = "taxiBooking-api")
public interface TaxiService {

    @GET
    List<Taxi> getAllTaxi(@Parameter(required = false)@QueryParam(value = "registration") String registration);


}