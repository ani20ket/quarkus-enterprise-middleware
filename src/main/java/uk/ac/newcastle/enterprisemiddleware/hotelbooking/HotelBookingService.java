package uk.ac.newcastle.enterprisemiddleware.hotelbooking;


import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Dependent
public class HotelBookingService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelBookingRepository crud;

    @Inject
    HotelBookingValidator validator;

    @Inject
    CustomerService customerService;

    @Inject
    HotelService hotelService;


    /**
     * Retrieves all hotel bookings for a specific customer identified by the customerId.
     *
     * @param customerId the ID of the customer whose bookings are to be fetched
     * @return a list of {@link HotelBooking} objects associated with the given customer ID
     */
    public List<HotelBooking> findAllBookingsByCustomerId(long customerId) {
        log.info("Finding all bookings by customer id: " + customerId);

        return crud.findAllByCustomerId(customerId);
    }

    /**
     * Creates a new hotel booking from the provided HotelBookingDTO.
     * Validates the booking data and persists it to the database.
     *
     * @param hotelBookingDTO the data transfer object containing booking details
     * @return the created {@link HotelBooking} object
     * @throws Exception if the booking data is invalid or if there are issues during the creation process
     */
    public HotelBooking create(HotelBookingDTO hotelBookingDTO) throws Exception {
        log.info("HotelService.create() - Creating " + hotelBookingDTO.getHotelId());

        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setHotelId(hotelBookingDTO.getHotelId());
        hotelBooking.setNoOfRooms(hotelBookingDTO.getNoOfRooms());
        hotelBooking.setCheckInDate(hotelBookingDTO.getCheckInDate());
        hotelBooking.setCheckOutDate(hotelBookingDTO.getCheckOutDate());
        hotelBooking.setNoOfGuests(hotelBookingDTO.getNoOfGuests());

        Customer customer = customerService.findById(Long.valueOf(hotelBookingDTO.getCustomerId()));

        hotelBooking.setCustomer(customer);

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateHotel(hotelBooking);

        // Write the hotelBooking to the database.
        return crud.create(hotelBooking);
    }

    /**
     * Creates a new hotel booking directly from a {@link HotelBooking} object.
     * Validates the booking data and persists it to the database.
     *
     * @param hotelBooking the hotel booking object to be created
     * @return the created {@link HotelBooking} object
     * @throws Exception if the booking data is invalid or if there are issues during the creation process
     */
    public HotelBooking createBooking(HotelBooking hotelBooking) throws Exception {
        log.info("HotelService.create() - Creating " + hotelBooking.getHotelId());

        // Check to make sure the data fits with the parameters in the Contact model and passes validation.
        validator.validateHotel(hotelBooking);

        // Write the hotelBooking to the database.
        return crud.create(hotelBooking);
    }

    /**
     * Deletes the given hotel booking from the database.
     *
     * @param hotelBooking the {@link HotelBooking} object to be deleted
     */
    public void deleteBooking(HotelBooking hotelBooking) {
        log.info("HotelService.deleteBooking() - Deleting " + hotelBooking.getHotelId());
        crud.deleteBooking(hotelBooking);

    }

    /**
     * Finds a hotel booking by its ID.
     *
     * @param hotelBookingId the ID of the hotel booking to retrieve
     * @return the {@link HotelBooking} object with the given ID, or null if not found
     */
    public HotelBooking findById(String hotelBookingId) {
        log.info("HotelService.findById() - Finding " + hotelBookingId);
        return crud.findById(Long.valueOf(hotelBookingId));
    }

    /**
     * Creates a new hotel booking through an agent from the provided HotelGuestBookingDTO.
     * The agent selects a hotel based on the postcode and creates a booking for the customer.
     *
     * @param hotelGuestBookingDTO the data transfer object containing booking details, including customer and hotel info
     * @return the created {@link HotelBooking} object
     * @throws Exception if no hotel is found for the provided postcode or if there are issues during the booking process
     */
    public HotelBooking createAgentBooking(HotelGuestBookingDTO hotelGuestBookingDTO) throws Exception {
        List<Hotel> hotels = hotelService.findAll();

        Customer customer = customerService.findById(Long.valueOf(hotelGuestBookingDTO.getCustomerId()));

        Optional<Hotel> hotelOptional = hotels.stream().filter(h -> h.getPostCode().equals(hotelGuestBookingDTO.getPostCode())).findFirst();

        if(hotelOptional.isPresent()){
            Hotel hotel = hotelOptional.get();
            HotelBooking hotelBooking = new HotelBooking();
            hotelBooking.setHotelId(String.valueOf(hotel.getId()));
            hotelBooking.setCustomer(customer);
            hotelBooking.setNoOfRooms(hotelGuestBookingDTO.getNoOfRooms());
            hotelBooking.setCheckInDate(hotelGuestBookingDTO.getCheckInDate());
            hotelBooking.setCheckOutDate(hotelGuestBookingDTO.getCheckOutDate());
            hotelBooking.setNoOfGuests(hotelGuestBookingDTO.getNoOfGuests());
            return createBooking(hotelBooking);
        }else{
            throw new RuntimeException("Hotel Not Found");
        }
    }

}
