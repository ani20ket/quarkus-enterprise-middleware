package uk.ac.newcastle.enterprisemiddleware.flight;

/**
 * Represents a flight with information about the departure and destination locations,
 * as well as the unique flight identifier.
 */
public class Flight {
    private String departure;
    private String destination;
    private Long flightId;


    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }
}
