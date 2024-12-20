package uk.ac.newcastle.enterprisemiddleware.hotelbooking;


import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelRepository;

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
 * <p>This class provides methods to check HotelBooking objects against arbitrary requirements.</p>
 *
 * @see HotelBooking
 * @see HotelBookingRepository
 * @see Validator
 */
@ApplicationScoped
public class HotelBookingValidator {
    @Inject
    Validator validator;

    @Inject
    HotelRepository hotelRepository;

    @Inject
    HotelBookingRepository hotelBookingRepository;

    /**
     * Validates the provided hotel booking data.
     * <p>
     * This method checks if the hotel booking adheres to the validation constraints defined in the
     * {@link HotelBooking} class. It also ensures that the provided hotel ID is valid (i.e., that the hotel exists in the database).
     * If any validation constraints are violated, a {@link ConstraintViolationException} is thrown. If the hotel ID is invalid or not found,
     * a {@link HotelNotFoundException} is thrown.
     *
     * @param hotelBooking the {@link HotelBooking} object to be validated
     * @throws ConstraintViolationException if the validation of the hotel booking fails
     * @throws ValidationException if the hotel ID is not found in the database
     */
    void validateHotel(HotelBooking hotelBooking) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<HotelBooking>> violations = validator.validate(hotelBooking);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the phone address
        else if (!hotelIdNotPresent(hotelBooking.getHotelId())) {
            throw new HotelNotFoundException("Hotel not found");
        }else if(hotelBookingConflict(hotelBooking)){
            throw new HotelNotFoundException("Hotel conflict");
        }
    }

    private boolean hotelBookingConflict(HotelBooking hotelBooking) {
        HotelBooking hotelBookingInDB = null;
        try{
            hotelBookingInDB = hotelBookingRepository.findByHotelIdAndDate(hotelBooking.getHotelId(), hotelBooking.getCheckInDate());
        }catch(NoResultException e){
            //ignore
        }
        return hotelBookingInDB != null;

    }

    /**
     * Checks if a hotel with the given ID exists in the database.
     * <p>
     * This method attempts to find a hotel by its ID. If no hotel is found, the method returns false, indicating that the hotel ID
     * is invalid or non-existent. Otherwise, it returns true.
     *
     * @param hotelId the ID of the hotel to check
     * @return {@code true} if the hotel exists in the database, {@code false} otherwise
     */
    private boolean hotelIdNotPresent(String hotelId) {
        Hotel hotel = null;
        try {
            hotel = hotelRepository.findById(Long.valueOf(hotelId));
        } catch (NoResultException e) {
            // ignore
        }
        return hotel != null;
    }

}

