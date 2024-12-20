package uk.ac.newcastle.enterprisemiddleware.hotelbooking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * REST service for managing {@link HotelBooking} entities.
 * Provides endpoints to create, retrieve, and delete hotel bookings.
 *
 * <p>This service is accessed at the base path {@code /hotel-bookings} and consumes/produces JSON.</p>
 *
 */
@Path("/hotel-bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelBookingRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelBookingService service;

    /**
     * Retrieves all hotel bookings associated with a specified customer ID.
     *
     * @param id the ID of the customer whose bookings are to be fetched
     * @return a JSON array of {@link HotelBooking} objects
     * @throws RestServiceException if an invalid ID is supplied or an unexpected error occurs
     */
    @GET
    @Operation(summary = "Fetch all HotelBooking Rooms", description = "Returns a JSON array of all stored HotelBooking objects.")
    @Path("/{id:[0-9]+}")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "HotelBooking retrieved successfully."),
            @APIResponse(responseCode = "400", description = "Invalid customerId supplied in request body"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    public Response retrieveAllHotelRooms(@Parameter(description = "Id of Customer to be fetched")
                                              @Schema(minimum = "0", required = true)
                                              @PathParam("id")long id) {
        List<HotelBooking> hotels = service.findAllBookingsByCustomerId(id);

        return Response.ok(hotels).build();
    }

    /**
     * Adds a new hotel booking to the database.
     *
     * @param hotel a {@link HotelBookingDTO} representing the hotel booking to add
     * @return a 201 response with the created {@link HotelBooking} if successful
     * @throws RestServiceException if the request body is invalid or if a conflict occurs
     */
    @POST
    @Operation(description = "Add a new HotelBooking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "HotelBooking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid HotelBooking supplied in request body"),
            @APIResponse(responseCode = "409", description = "HotelBooking supplied in request body conflicts with an existing HotelBooking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotelBooking(
            @Parameter(description = "JSON representation of HotelBooking object to be added to the database", required = true)
            HotelBookingDTO hotel) {

        if (hotel == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {

            // Go add the new HotelBooking.
            HotelBooking hotelBooking = service.create(hotel);

            // Create a "Resource Created" 201 Response and pass the hotelBooking back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotelBooking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createHotel completed. HotelBooking = " + hotel);
        return builder.build();
    }

    /**
     * Deletes a hotel booking from the database.
     *
     * @param id the ID of the hotel booking to delete
     * @return a 204 response if the booking is deleted successfully
     * @throws RestServiceException if the booking is not found or an error occurs during deletion
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a HotelBooking from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotelBooking has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid HotelBooking id supplied"),
            @APIResponse(responseCode = "404", description = "HotelBooking with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteContact(
            @Parameter(description = "Id of HotelBooking to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            String id) {

        Response.ResponseBuilder builder;

        HotelBooking hotelBooking = service.findById(id);
        if (hotelBooking == null) {
            // Verify that the hotelBooking exists. Return 404, if not present.
            throw new RestServiceException("No HotelBooking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.deleteBooking(hotelBooking);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteContact completed. HotelBooking = " + hotelBooking);
        return builder.build();
    }
}
