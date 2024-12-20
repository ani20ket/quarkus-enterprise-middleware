package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.customer.UniqueEmailException;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * REST endpoint for creating a new GuestBooking. The method processes the incoming
 * request, validates the input, creates a new GuestBooking in the database, and returns
 * a response with the appropriate status.
 */
@Path("/guest-booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GuestBookingRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    GuestBookingService service;

    @POST
    @Operation(description = "Add a new GuestBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "GuestBooking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid GuestBooking supplied in request body"),
            @APIResponse(responseCode = "409", description = "GuestBooking supplied in request body conflicts with an existing GuestBooking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotel(
            @Parameter(description = "JSON representation of GuestBooking object to be added to the database", required = true)
            GuestBooking guestBooking) {

        if (guestBooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {

            // Go add the new GuestBooking.
            HotelBooking hotelBooking = service.createGuestBooking(guestBooking);

            // Create a "Resource Created" 201 Response and pass the contact back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotelBooking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueEmailException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createGuestBooking completed. GuestBooking = " + guestBooking);
        return builder.build();
    }
}
