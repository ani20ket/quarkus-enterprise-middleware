package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator class that provides methods to validate customer data,
 * including checking for data consistency and uniqueness constraints.
 */
@ApplicationScoped
public class CustomerValidator {
    @Inject
    Validator validator;

    @Inject
    CustomerRepository crud;

    /**
     * Validates the customer object for data integrity and uniqueness of the email address.
     *
     * @param customer the customer object to be validated
     * @throws ConstraintViolationException if any validation constraints are violated
     * @throws ValidationException if the email address is not unique
     */
    void validateCustomer(Customer customer) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the email address
        if (emailAlreadyExists(customer.getEmail(), customer.getId())) {
            throw new UniqueEmailException("Unique Email Violation");
        }
    }

    /**
     * Checks if an email address already exists in the database, excluding the specified customer ID.
     *
     * @param email the email address to check
     * @param id the ID of the customer to exclude from the check, or null if not applicable
     * @return true if the email address already exists and belongs to a different customer, false otherwise
     */
    boolean emailAlreadyExists(String email, Long id) {
        Customer customer = null;
        Customer customerWithID = null;
        try {
            customer = crud.findByEmail(email);
        } catch (NoResultException e) {
            // ignore
        }

        if (customer != null && id != null) {
            try {
                customerWithID = crud.findById(id);
                if (customerWithID != null && customerWithID.getEmail().equals(email)) {
                    customer = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return customer != null;
    }
}

