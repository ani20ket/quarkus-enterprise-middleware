package uk.ac.newcastle.enterprisemiddleware.hotel;


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
 * This class provides methods to check Hotel objects against arbitrary requirements.
 *
 * @see Hotel
 * @see HotelRepository
 * @see Validator
 */
@ApplicationScoped
public class HotelValidator {
    @Inject
    Validator validator;

    @Inject
    HotelRepository crud;

    /**
     * Validates a hotel object for any constraint violations and checks for phone number uniqueness.
     *
     * @param hotel the {@link Hotel} object to be validated.
     * @throws ConstraintViolationException if any validation constraints are violated.
     * @throws ValidationException if any other validation exception occurs.
     * @throws UniquePhonelException if the phone number is already in use by another hotel.
     */
    void validateHotel(Hotel hotel) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Hotel>> violations = validator.validate(hotel);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the phone address
        if (phoneAlreadyExists(hotel.getPhoneNumber(), hotel.getId())) {
            throw new UniquePhonelException("Unique Phone Violation");
        }
    }

    /**
     * Checks if a phone number is already associated with another hotel in the database.
     *
     * @param phone the phone number to be checked.
     * @param id the ID of the hotel (to exclude this hotel from the check if updating an existing record).
     * @return true if the phone number is already associated with another hotel, false otherwise.
     */
    boolean phoneAlreadyExists(String phone, Long id) {
        Hotel hotel = null;
        Hotel hotelWithID = null;
        try {
            hotel = crud.findAllByPhoneNumber(phone);
        } catch (NoResultException e) {
            // ignore
        }

        if (hotel != null && id != null) {
            try {
                hotelWithID = crud.findById(id);
                if (hotelWithID != null && hotelWithID.getPhoneNumber().equals(phone)) {
                    hotel = null;
                }
            } catch (NoResultException e) {
                // ignore
            }
        }
        return hotel != null;
    }
}

