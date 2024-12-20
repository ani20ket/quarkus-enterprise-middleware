package uk.ac.newcastle.enterprisemiddleware.hotel;

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
 * RESTful service class for managing hotel-related operations. This class defines endpoints
 * for creating, retrieving, updating, and deleting {@link Hotel} entities. All endpoints
 * consume and produce JSON.
 *
 */
@Path("/hotels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelService service;


    /**
     * Retrieves all hotel rooms from the database.
     *
     * @return a Response containing a JSON array of all stored {@link Hotel} objects with HTTP status 200 (OK).
     */
    @GET
    @Operation(summary = "Fetch all Hotel Rooms", description = "Returns a JSON array of all stored Hotel objects.")
    public Response retrieveAllHotelRooms() {
        //Create an empty collection to contain the intersection of Hotels to be returned
        List<Hotel> hotels = service.findAll();

        return Response.ok(hotels).build();
    }

    /**
     * Adds a new hotel to the database.
     *
     * @param hotel the {@link Hotel} object to be added, represented as JSON in the request body.
     * @return a Response with HTTP status 201 (Created) if the hotel was successfully added, or
     *         an appropriate error response if validation fails or a unique constraint is violated.
     * @throws RestServiceException if there is an issue with the request data or if any other unexpected error occurs.
     */
    @POST
    @Operation(description = "Add a new Hotel to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Hotel created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "409", description = "Hotel supplied in request body conflicts with an existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotel(
            @Parameter(description = "JSON representation of Hotel object to be added to the database", required = true)
            Hotel hotel) {

        if (hotel == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            hotel.setId(null);
            // Go add the new Hotel.
            service.create(hotel);

            // Create a "Resource Created" 201 Response and pass the contact back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(hotel);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniquePhonelException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That phone is already used, please use a phone email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createHotel completed. Hotel = " + hotel);
        return builder.build();
    }

    /**
     * Deletes an existing hotel from the database by its ID.
     *
     * @param id the ID of the {@link Hotel} to be deleted, passed as a path parameter.
     * @return a Response with HTTP status 204 (No Content) if deletion is successful, or
     *         an appropriate error response if the hotel ID is not found or if an error occurs during deletion.
     * @throws RestServiceException if the hotel ID is not found or if any other unexpected error occurs.
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a Hotel from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotel has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel id supplied"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteContact(
            @Parameter(description = "Id of Hotel to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            String id) throws Exception {

        Response.ResponseBuilder builder;

        Hotel hotel = service.findById(id);
        if (hotel == null) {
            // Verify that the hotel exists. Return 404, if not present.
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.deleteBooking(hotel);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteContact completed. Hotel = " + hotel);
        return builder.build();
    }
}
