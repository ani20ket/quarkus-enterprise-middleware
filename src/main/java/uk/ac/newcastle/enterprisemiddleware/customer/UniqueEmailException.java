package uk.ac.newcastle.enterprisemiddleware.customer;


import javax.validation.ValidationException;

/**
 * <p>ValidationException caused if a Customer's email address conflicts with that of another Customer.</p>
 *
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class UniqueEmailException extends ValidationException {

    public UniqueEmailException(String message) {
        super(message);
    }
}

