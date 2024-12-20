package uk.ac.newcastle.enterprisemiddleware.guestbooking;


import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class GuestBooking {
    @NotNull
    private CustomerDTO customer;

    @NotNull
    @NotEmpty
    private String hotelId;

    @NotNull
    @Future(message = "The check in date must be future")
    private LocalDate checkInDate;

    @NotNull
    @Future(message = "The check out date must be future")
    private LocalDate checkOutDate;

    @NotNull
    private long noOfGuests;

    @NotNull
    private long noOfRooms;

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public @NotNull @Future(message = "The check in date must be future") LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(@NotNull @Future(message = "The check in date must be future") LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public @NotNull @Future(message = "The check out date must be future") LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(@NotNull @Future(message = "The check out date must be future") LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @NotNull
    public long getNoOfGuests() {
        return noOfGuests;
    }

    public void setNoOfGuests(@NotNull long noOfGuests) {
        this.noOfGuests = noOfGuests;
    }

    @NotNull
    public long getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(@NotNull long noOfRooms) {
        this.noOfRooms = noOfRooms;
    }
}
