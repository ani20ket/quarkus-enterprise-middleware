package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class HotelDTO {
    @NotNull
    @Size(min = 1, max = 50)
    private String hotelName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$")
    private String postCode;

    @NotNull
    @Pattern(regexp = "^0\\d{10}$")
    private String phoneNumber;

    public @NotNull @Size(min = 1, max = 50) String getHotelName() {
        return hotelName;
    }

    public void setHotelName(@NotNull @Size(min = 1, max = 50) String hotelName) {
        this.hotelName = hotelName;
    }

    public @NotNull @Pattern(regexp = "^[a-zA-Z0-9]{6}$") String getPostCode() {
        return postCode;
    }

    public void setPostCode(@NotNull @Pattern(regexp = "^[a-zA-Z0-9]{6}$") String postCode) {
        this.postCode = postCode;
    }

    public @NotNull @Pattern(regexp = "^0\\d{10}$") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull @Pattern(regexp = "^0\\d{10}$") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
