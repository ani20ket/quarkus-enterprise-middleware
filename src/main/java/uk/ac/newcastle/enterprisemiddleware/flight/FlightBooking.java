package uk.ac.newcastle.enterprisemiddleware.flight;

/**
 * Represents a FlightBooking with information about the departure and destination locations,
 * as well as the unique FlightBooking identifier.
 */
public class FlightBooking {
    private Long bookingId;
    private FlightDTO flight;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
    }
}
