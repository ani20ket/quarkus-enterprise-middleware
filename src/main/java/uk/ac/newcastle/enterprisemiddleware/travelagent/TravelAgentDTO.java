package uk.ac.newcastle.enterprisemiddleware.travelagent;


import uk.ac.newcastle.enterprisemiddleware.flight.FlightDTO;
import uk.ac.newcastle.enterprisemiddleware.guestbooking.CustomerDTO;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelGuestBookingDTO;
import uk.ac.newcastle.enterprisemiddleware.taxi.Taxi;

import java.time.LocalDate;

public class TravelAgentDTO {
    private CustomerDTO customer;
    private HotelGuestBookingDTO hotel;
    private Taxi taxi;
    private FlightDTO flightDTO;

    private LocalDate date;

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public HotelGuestBookingDTO getHotel() {
        return hotel;
    }

    public void setHotel(HotelGuestBookingDTO hotel) {
        this.hotel = hotel;
    }

    public FlightDTO getFlightDTO() {
        return flightDTO;
    }

    public void setFlightDTO(FlightDTO flightDTO) {
        this.flightDTO = flightDTO;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }
}
