package uk.ac.newcastle.enterprisemiddleware.customer;


import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;


/**
 * Service class that provides business logic for managing customer operations,
 * including creation, retrieval, and deletion of customers.
 *
 * It interacts with the CustomerRepository for data access and the CustomerValidator for validation checks.
 */
@Dependent
public class CustomerService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerValidator validator;

    @Inject
    CustomerRepository crud;

    /**
     * Retrieves all customers ordered by name.
     *
     * @return a list of all customers ordered by name
     */
    List<Customer> findAllOrderedByName() {
        return crud.findAllOrderedByName();
    }

    /**
     * Finds a customer by their unique ID.
     *
     * @param id the ID of the customer to find
     * @return the customer with the specified ID, or null if not found
     */
    public Customer findById(Long id) {
        return crud.findById(id);
    }

    /**
     * Finds a customer by their email address.
     *
     * @param email the email address of the customer to find
     * @return the customer with the specified email, or null if not found
     */
    public Customer findByEmail(String email) {
        return crud.findByEmail(email);
    }

    /**
     * Finds all customers with the specified first name.
     *
     * @param name the first name of the customers to find
     * @return a list of customers with the specified first name
     */
    List<Customer> findAllByFirstName(String name) {
        return crud.findAllByName(name);
    }

    /**
     * Creates a new customer after validating the customer data.
     *
     * @param customer the customer to be created
     * @return the created customer object
     * @throws Exception if validation fails or an error occurs during creation
     */
    public Customer create(Customer customer) throws Exception {
        log.info("CustomerService.create() - Creating " + customer.getName());

        // Check to make sure the data fits with the parameters in the Customer model and passes validation.
        validator.validateCustomer(customer);

        // Write the customer to the database.
        return crud.create(customer);
    }

    /**
     * Deletes the specified customer from the database.
     *
     * @param customer the customer to be deleted
     * @throws Exception if an error occurs during deletion
     */
    public void delete(Customer customer) throws Exception {
        log.info("delete() - Deleting " + customer.toString());

        crud.delete(customer);

    }
}
