package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * RESTful service for managing Travel Agent bookings.
 * <p>
 * This class provides endpoints for interacting with `TravelAgent` entities via HTTP requests.
 * It includes operations to retrieve all `TravelAgent` bookings associated with a specific customer ID.
 * </p>
 */
@Path("/travel-agent")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    TravelAgentService travelAgentService;

    /**
     * Fetches all TravelAgent bookings for a specified customer ID.
     *
     * @param id the ID of the customer whose TravelAgent bookings are to be fetched
     * @return a Response containing a JSON array of TravelAgent bookings, or an error response if an issue occurs
     */
    @GET
    @Operation(summary = "Fetch all TravelAgentBookings by customer Id", description = "Returns a JSON array of all stored TravelAgentBookings objects.")
    @Path("/{id:[0-9]+}")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "TravelAgentBookings retrieved successfully."),
            @APIResponse(responseCode = "400", description = "Invalid customerId supplied in request body"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    public Response retrieveAllBookings(@Parameter(description = "Id of Customer to be fetched")
                                              @Schema(minimum = "0", required = true)
                                              @PathParam("id")long id) {
        List<TravelAgent> hotels = travelAgentService.findAllBookingsByCustomerId(id);

        return Response.ok(hotels).build();
    }

    /**
     * Creates a new TravelAgent booking and adds it to the database.
     *
     * @param travelAgentDTO the JSON representation of the TravelAgentDTO object to be added to the database
     * @return a Response containing the created TravelAgent booking object
     * @throws RestServiceException if the TravelAgentDTO is invalid or conflicts with an existing booking
     */
    @POST
    @Operation(description = "Add a new TravelAgent Booking to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "TravelAgent Booking created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid TravelAgentDTO supplied in request body"),
            @APIResponse(responseCode = "409", description = "TravelAgentDTO supplied in request body conflicts with an existing HotelBooking"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createBooking(
            @Parameter(description = "JSON representation of TravelAgentDTO object to be added to the database", required = true)
            TravelAgentDTO travelAgentDTO) {

        if (travelAgentDTO == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {

            // Go add the new HotelBooking.
            TravelAgent travelAgent = travelAgentService.createBooking(travelAgentDTO);

            // Create a "Resource Created" 201 Response and pass the hotelBooking back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(travelAgent);


        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e.getMessage(), Response.Status.CONFLICT);
        }

        log.info("createTravelAgentBooking completed.");
        return builder.build();
    }

    /**
     * Deletes a TravelAgent booking by its ID.
     *
     * @param id the ID of the TravelAgent booking to be deleted
     * @return a Response indicating the result of the deletion operation
     * @throws RestServiceException if the booking ID is invalid or the booking is not found
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a TravelAgentBooking from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The TravelAgentBooking has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid TravelAgentBooking id supplied"),
            @APIResponse(responseCode = "404", description = "TravelAgentBooking with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteBooking(
            @Parameter(description = "Id of HotelBooking to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            String id) {

        Response.ResponseBuilder builder;

        TravelAgent travelAgent = travelAgentService.findById(id);
        if (travelAgent == null) {
            // Verify that the hotelBooking exists. Return 404, if not present.
            throw new RestServiceException("No TravelAgentObject with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            travelAgentService.deleteBooking(travelAgent);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteBooking completed. travelAgent = " + travelAgent);
        return builder.build();
    }
}
