package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBookingService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.NoResultException;

@Dependent
public class GuestBookingService {
    @Inject
    CustomerService customerService;

    @Inject
    HotelBookingService hotelBookingService;

    /**
     * Creates a new GuestBooking. The method checks if the customer already exists in the database
     * based on the customer's email. If the customer does not exist, a new customer is created.
     * It then creates a new `HotelBooking` associated with the customer and returns the created
     * `HotelBooking` object.
     *
     * @param guestBooking the GuestBooking object containing booking details
     * @return the created HotelBooking object
     * @throws Exception if an error occurs during the creation of the booking or customer
     */
    public HotelBooking createGuestBooking(GuestBooking guestBooking) throws Exception {
        String customerEmail = guestBooking.getCustomer().getEmail();
        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setHotelId(guestBooking.getHotelId());
        hotelBooking.setNoOfGuests(guestBooking.getNoOfGuests());
        hotelBooking.setCheckInDate(guestBooking.getCheckInDate());
        hotelBooking.setCheckOutDate(guestBooking.getCheckOutDate());
        hotelBooking.setNoOfRooms(guestBooking.getNoOfRooms());
        Customer customer = null;
        try{
            customer = customerService.findByEmail(customerEmail);
        } catch (NoResultException e) {
            CustomerDTO customerDTO = guestBooking.getCustomer();
            customer = new Customer();
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer = customerService.create(customer);
        }
        hotelBooking.setCustomer(customer);
        return hotelBookingService.createBooking(hotelBooking);
    }
}
