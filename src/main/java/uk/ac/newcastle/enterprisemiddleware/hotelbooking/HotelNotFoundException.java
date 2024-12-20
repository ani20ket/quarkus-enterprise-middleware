package uk.ac.newcastle.enterprisemiddleware.hotelbooking;


import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.validation.ValidationException;

/**
 * <p>ValidationException caused if a Hotel's phone number conflicts with that of another Hotel.</p>
 *
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Customer
 */
public class HotelNotFoundException extends ValidationException {

    public HotelNotFoundException(String message) {
        super(message);
    }

    public HotelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HotelNotFoundException(Throwable cause) {
        super(cause);
    }
}

