package uk.ac.newcastle.enterprisemiddleware.travelagent;


import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.flight.FlightBooking;
import uk.ac.newcastle.enterprisemiddleware.flight.FlightBookingAgentService;
import uk.ac.newcastle.enterprisemiddleware.guestbooking.CustomerDTO;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBooking;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBookingService;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiAgentBookingService;
import uk.ac.newcastle.enterprisemiddleware.taxi.TaxiBooking;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for managing TravelAgent bookings.
 * <p>
 * This service coordinates the creation, retrieval, and deletion of TravelAgent bookings
 * by interacting with various booking services (Flight, Taxi, Hotel) and the TravelAgentRepository.
 * </p>
 */
@Dependent
public class TravelAgentService {

    private static String AGENT_NAME = "AniketTravelAgent";
    private static String AGENT_EMAIL = "aniket@agent.com";
    private static String AGENT_PHONE = "07717068902";

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    FlightBookingAgentService flightBookingAgentService;

    @Inject
    CustomerService customerService;

    @Inject
    TaxiAgentBookingService taxiAgentBookingService;

    @Inject
    HotelBookingService hotelBookingService;

    @Inject
    TravelAgentRepository travelAgentRepository;

    /**
     * Creates a new TravelAgent booking by associating the customer with taxi, hotel, and flight bookings.
     * If any of the steps fail, the previous bookings are reverted to maintain data consistency.
     *
     * @param travelAgentDTO the DTO containing all information to create the TravelAgent booking
     * @return the created TravelAgent object
     * @throws Exception if any error occurs during the creation process
     */
    public TravelAgent createBooking(TravelAgentDTO travelAgentDTO) throws Exception {
        TravelAgent travelAgent = new TravelAgent();

        // Validate that the customer DTO is not null
        CustomerDTO customerDTO = travelAgentDTO.getCustomer();
        if (customerDTO == null) {
            throw new IllegalArgumentException("Customer DTO is null");
        }

        Customer customer = null;

        try {
            // Try to find an existing customer by email
            customer = customerService.findByEmail(customerDTO.getEmail());
        } catch (Exception e) {
            // If customer doesn't exist, create a new customer
            Customer newCustomer = new Customer();
            newCustomer.setEmail(customerDTO.getEmail());
            newCustomer.setName(customerDTO.getName());
            newCustomer.setPhoneNumber(customerDTO.getPhoneNumber());
            customer = customerService.create(newCustomer);
        }

        // Set the customerId in the TravelAgent object
        travelAgent.setCustomerId(customer.getId());

        try {
            // Create the taxi booking and set its ID in the TravelAgent object
            TaxiBooking taxiBooking = taxiAgentBookingService.createTaxiBooking(AGENT_NAME, AGENT_EMAIL, AGENT_PHONE,
                    travelAgentDTO.getTaxi().getNumberOfSeats(), travelAgentDTO.getDate());
            travelAgent.setTaxiBookingId(taxiBooking.getId());
        } catch (Exception e) {
            // If taxi booking creation fails, revert customer creation and throw an error
            revertCustomer(travelAgent.getCustomerId());
            throw new RuntimeException("Taxi not found");
        }

        try {
            // Create the hotel booking and set its ID in the TravelAgent object
            travelAgentDTO.getHotel().setCustomerId(String.valueOf(customer.getId()));
            HotelBooking hotelBooking = hotelBookingService.createAgentBooking(travelAgentDTO.getHotel());
            travelAgent.setHotelBookingId(hotelBooking.getId());
        } catch (Exception e) {
            // If hotel booking creation fails, revert taxi and customer, then throw an error
            revertTaxi(travelAgent.getTaxiBookingId());
            revertCustomer(travelAgent.getCustomerId());
            throw new RuntimeException("Hotel not found");
        }

        try {
            // Create the flight booking and set its ID in the TravelAgent object
            FlightBooking flightBooking = flightBookingAgentService.createAgentBooking(AGENT_NAME, AGENT_EMAIL, AGENT_PHONE,
                    travelAgentDTO.getFlightDTO(), travelAgentDTO.getDate());
            travelAgent.setFlightBookingId(flightBooking.getBookingId());
        } catch (Exception e) {
            // If flight booking creation fails, revert hotel, taxi, and customer, then throw an error
            revertHotel(travelAgent.getHotelBookingId());
            revertTaxi(travelAgent.getTaxiBookingId());
            revertCustomer(travelAgent.getCustomerId());
            throw new RuntimeException("Flight not found");
        }

        // Set the date for the TravelAgent booking
        travelAgent.setDate(travelAgentDTO.getDate());

        // Persist the TravelAgent object and return it
        return travelAgentRepository.create(travelAgent);
    }

    /**
     * Deletes a TravelAgent booking along with its associated taxi, hotel, and flight bookings.
     * If any of the related bookings cannot be deleted, the method rolls back the deletions
     * for all bookings and the customer.
     *
     * @param travelAgent the TravelAgent booking to be deleted.
     * @throws RuntimeException if an error occurs during the deletion process.
     */
    public void deleteBooking(TravelAgent travelAgent) {

        log.info("Deleting TravelAgent Booking with ID: " + travelAgent.getId());
        try {
            // Revert taxi booking
            revertTaxi(travelAgent.getTaxiBookingId());

            // Revert hotel booking
            revertHotel(travelAgent.getHotelBookingId());

            // Delete flight booking
            flightBookingAgentService.deleteBooking(travelAgent.getFlightBookingId());

            // Optionally, you could delete the TravelAgent itself after all bookings are deleted
            travelAgentRepository.delete(travelAgent);
            log.info("Successfully deleted TravelAgent Booking with ID: " + travelAgent.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete booking and related entries", e);
        }
    }

    /**
     * Reverts a hotel booking by deleting the specified hotel booking record.
     *
     * @param hotelBookingId the ID of the hotel booking to be reverted.
     * @throws RuntimeException if an error occurs during the deletion process.
     */
    private void revertHotel(Long hotelBookingId) {
        try {
            HotelBooking hotelBooking = hotelBookingService.findById(String.valueOf(hotelBookingId));
            if (hotelBooking != null) {
                hotelBookingService.deleteBooking(hotelBooking);
                log.info("Successfully reverted hotel booking with ID: " + hotelBookingId);
            } else {
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to revert hotel booking", e);
        }
    }

    /**
     * Reverts a taxi booking by deleting the specified taxi booking record.
     *
     * @param taxiBookingId the ID of the taxi booking to be reverted.
     * @throws RuntimeException if an error occurs during the deletion process.
     */
    private void revertTaxi(Long taxiBookingId) {
        try {
            taxiAgentBookingService.deleteTaxiBooking(taxiBookingId);
            log.info("Successfully reverted taxi booking with ID: " + taxiBookingId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to revert taxi booking", e);
        }
    }

    /**
     * Reverts a customer by deleting the specified customer record.
     *
     * @param customerId the ID of the customer to be reverted.
     * @throws Exception if an error occurs during the deletion process.
     */
    private void revertCustomer(Long customerId) throws Exception {
        try {
            Customer customer = customerService.findById(customerId);
            if (customer != null) {
                customerService.delete(customer);
                log.info("Successfully reverted customer with ID: " + customerId);
            } else {
                log.info("Customer not found for ID: " + customerId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to revert customer", e);
        }
    }

    /**
     * Finds all TravelAgent bookings associated with a given customer ID.
     *
     * @param id the customer ID whose bookings are to be fetched.
     * @return a list of TravelAgent bookings associated with the given customer ID.
     */
    public List<TravelAgent> findAllBookingsByCustomerId(long id) {
        log.info("Fetching all bookings for customer ID: " + id);
        return travelAgentRepository.findByCustomerId(id);
    }

    /**
     * Finds a TravelAgent booking by its ID.
     *
     * @param id the ID of the TravelAgent booking to be fetched.
     * @return the TravelAgent booking with the specified ID.
     */
    public TravelAgent findById(String id) {
        log.info("Fetching TravelAgent booking by ID: " + id);
        return travelAgentRepository.findById(Long.valueOf(id));
    }
}
