package uk.ac.newcastle.enterprisemiddleware.hotel;


import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.validation.ValidationException;

/**
 * <p>ValidationException caused if a Customer's email address conflicts with that of another Customer.</p>
 *
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class UniquePhonelException extends ValidationException {

    public UniquePhonelException(String message) {
        super(message);
    }

    public UniquePhonelException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniquePhonelException(Throwable cause) {
        super(cause);
    }
}

