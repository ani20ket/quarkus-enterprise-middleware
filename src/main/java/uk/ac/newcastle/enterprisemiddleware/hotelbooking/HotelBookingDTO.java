package uk.ac.newcastle.enterprisemiddleware.hotelbooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class HotelBookingDTO {

    private String hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long noOfGuests;
    private long noOfRooms;
    private String customerId;

    public @NotNull @Size(min = 1, max = 25) String getHotelId() {
        return hotelId;
    }

    public void setHotelId(@NotNull @Size(min = 1, max = 25) String hotelId) {
        this.hotelId = hotelId;
    }

    public @NotNull LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(@NotNull LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public @NotNull LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(@NotNull LocalDate checkOutDate) {
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
