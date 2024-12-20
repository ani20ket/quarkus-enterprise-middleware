package uk.ac.newcastle.enterprisemiddleware.flight;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling flight bookings through an agent. It interacts with
 * colleagues flight service to create and delete bookings.
 */
@Dependent
public class FlightBookingAgentService {

    @RestClient
    FlightService flightService;

    @RestClient
    FlightBookingService flightBookingService;

    @RestClient
    FlightGuestBookingService flightGuestBookingService;

    /**
     * Creates a flight booking through the agent by matching the provided flight details
     * with available flights, and then booking the flight for the specified agent.
     *
     * @param agentName the name of the agent making the booking
     * @param agentEmail the email address of the agent
     * @param agentPhoneNumber the phone number of the agent
     * @param flightDTO the flight details (departure, destination)
     * @param date the date of the flight
     * @return a FlightBooking object representing the booked flight
     * @throws RuntimeException if the flight matching the details is not found
     */
    public FlightBooking createAgentBooking(String agentName, String agentEmail, String agentPhoneNumber, FlightDTO flightDTO, LocalDate date) {
        List<Flight> flights = flightService.getAllFlights();

        Optional<Flight> flight = flights.stream().filter(f -> (f.getDeparture().equals(flightDTO.getDeparture()))
                && f.getDestination().equals(flightDTO.getDestination())).findFirst();
        if (flight.isPresent()) {
            FlightGuestBookingRequest flightGuestBookingRequest = new FlightGuestBookingRequest();
            flightGuestBookingRequest.setFlightId(flight.get().getFlightId());
            flightGuestBookingRequest.setDate(date);
            flightGuestBookingRequest.setCustomerEmail(agentEmail);
            flightGuestBookingRequest.setCustomerPhoneNumber(agentPhoneNumber);
            flightGuestBookingRequest.setCustomerName(agentName);
            return flightGuestBookingService.bookFlight(flightGuestBookingRequest);
        }else{
            throw new RuntimeException("Flight not found");
        }
    }

    /**
     * Deletes a flight booking identified by the given flight booking ID.
     *
     * @param flightBookingId the ID of the flight booking to be deleted
     */
    public void deleteBooking(Long flightBookingId) {
        flightBookingService.deleteBooking(flightBookingId);
    }
}
