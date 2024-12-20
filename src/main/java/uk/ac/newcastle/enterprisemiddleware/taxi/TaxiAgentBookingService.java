package uk.ac.newcastle.enterprisemiddleware.taxi;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.guestbooking.CustomerDTO;

import javax.enterprise.context.Dependent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling taxi booking operations initiated by an agent.
 * <p>
 * This service interacts with external services to manage taxi bookings, including creating new bookings
 * and deleting existing ones. It uses the {@link TaxiGuestBookingService} to create bookings and the {@link TaxiService}
 * to find available taxis based on the number of seats requested. It also uses {@link TaxiBookingService} for deleting bookings.
 */
@Dependent
public class TaxiAgentBookingService {

    @RestClient
    TaxiGuestBookingService taxiGuestBookingService;

    @RestClient
    TaxiService taxiService;

    @RestClient
    TaxiBookingService taxiBookingService;

    /**
     * Creates a new taxi booking for an agent.
     * <p>
     * This method searches for an available taxi with enough seats for the given booking. If a suitable taxi is found,
     * a booking is made using the provided agent details and the selected taxi's information.
     *
     * @param agentName the name of the agent making the booking
     * @param agentEmail the email address of the agent
     * @param agentPhoneNumber the phone number of the agent
     * @param numberOfSeats the number of seats required for the booking
     * @param date the date of the booking
     * @return the created {@link TaxiBooking} object
     * @throws RuntimeException if no suitable taxi is found
     */
    public TaxiBooking createTaxiBooking(String agentName, String agentEmail, String agentPhoneNumber,
                                         long numberOfSeats, LocalDate date){
        List<Taxi> taxis = taxiService.getAllTaxi(null);

        Optional<Taxi> taxiOptional = taxis.stream().filter(taxi -> numberOfSeats <= taxi.getNumberOfSeats()).findFirst();

        if(taxiOptional.isPresent()){
            TaxiDTO taxiDTO = new TaxiDTO();
            taxiDTO.setTaxiId(taxiOptional.get().getId());
            taxiDTO.setDate(date);

            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setEmail(agentEmail);
            customerDTO.setPhoneNumber(agentPhoneNumber);
            customerDTO.setName(agentName);

            TaxiGuestBookingRequest taxiGuestBookingRequest = new TaxiGuestBookingRequest();
            taxiGuestBookingRequest.setBooking(taxiDTO);
            taxiGuestBookingRequest.setCustomer(customerDTO);
            return taxiGuestBookingService.bookTaxi(taxiGuestBookingRequest);
        }else{
            throw new RuntimeException("Taxi not found");
        }
    }


    /**
     * Deletes a taxi booking by its ID.
     * <p>
     * This method interacts with the {@link TaxiBookingService} to delete the booking associated with the given taxi booking ID.
     *
     * @param taxiBookingId the ID of the taxi booking to be deleted
     */
    public void deleteTaxiBooking(Long taxiBookingId) {
        taxiBookingService.deleteTaxi(taxiBookingId);
    }
}
