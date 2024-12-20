package uk.ac.newcastle.enterprisemiddleware.hotelbooking;


import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.validation.ValidationException;

/**
 * <p>ValidationException caused if a Customer's email address conflicts with that of another Customer.</p>
 *
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class CustomerNotFoundException extends ValidationException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotFoundException(Throwable cause) {
        super(cause);
    }
}

