package uk.ac.newcastle.enterprisemiddleware.taxi;

import uk.ac.newcastle.enterprisemiddleware.guestbooking.CustomerDTO;

public class TaxiGuestBookingRequest {
    private TaxiDTO booking;
    private CustomerDTO customer;

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public TaxiDTO getBooking() {
        return booking;
    }

    public void setBooking(TaxiDTO booking) {
        this.booking = booking;
    }
}
