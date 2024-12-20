package uk.ac.newcastle.enterprisemiddleware.customer;

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
 * RESTful service for managing customer resources.
 * Consumes and produces JSON representations of customer data.
 *
 * Provides endpoints for customer-related operations, such as creating, deleting and retrieving customers.
 */
@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerRestService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService service;

    /**
     * Fetches all customers or filters customers by name if a name is provided.
     *
     * @param name an optional query parameter to filter customers by name
     * @return a JSON response containing a list of customers, filtered by name if specified,
     *         or all customers ordered by name if no filter is applied
     */
    @GET
    @Operation(summary = "Fetch all Customers", description = "Returns a JSON array of all stored Customer objects.")
    public Response retrieveAllCustomers(@QueryParam("name") String name) {
        //Create an empty collection to contain the intersection of Customers to be returned
        List<Customer> customers;

        if(name == null) {
            customers = service.findAllOrderedByName();
        } else{
            customers = service.findAllByFirstName(name);
        }

        return Response.ok(customers).build();
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customer the JSON representation of the Customer object to be added to the database
     * @return a response indicating the outcome of the create operation:
     *         - 201 (Created) if the customer was successfully created
     *         - 400 (Bad Request) if the input is invalid or fails validation checks
     *         - 409 (Conflict) if a customer with the same unique email already exists
     *         - 500 (Internal Server Error) if an unexpected error occurs
     * @throws RestServiceException if a validation error or unique constraint violation occurs
     */
    @SuppressWarnings("unused")
    @POST
    @Operation(description = "Add a new Customer to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Customer supplied in request body"),
            @APIResponse(responseCode = "409", description = "Customer supplied in request body conflicts with an existing Customer"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createCustomer(
            @Parameter(description = "JSON representation of Customer object to be added to the database", required = true)
            Customer customer) {

        if (customer == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Clear the ID if accidentally set
            customer.setId(null);

            // Go add the new Customer.
            service.create(customer);

            // Create a "Resource Created" 201 Response and pass the customer back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(customer);


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

        log.info("createCustomer completed. Customer = " + customer);
        return builder.build();
    }

    /**
     * Deletes a customer from the database based on the specified customer ID.
     *
     * @param id the ID of the customer to be deleted
     * @return a response indicating the outcome of the delete operation:
     *         - 204 (No Content) if the customer was successfully deleted
     *         - 400 (Bad Request) if the ID supplied is invalid
     *         - 404 (Not Found) if no customer exists with the specified ID
     *         - 500 (Internal Server Error) if an unexpected error occurs
     * @throws RestServiceException if the customer is not found or if an error occurs during deletion
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a Customer from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The customer has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Customer id supplied"),
            @APIResponse(responseCode = "404", description = "Customer with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteCustomer(
            @Parameter(description = "Id of Customer to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        Customer customer = service.findById(id);
        if (customer == null) {
            // Verify that the customer exists. Return 404, if not present.
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.delete(customer);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteCustomer completed. Customer = " + customer);
        return builder.build();
    }
}