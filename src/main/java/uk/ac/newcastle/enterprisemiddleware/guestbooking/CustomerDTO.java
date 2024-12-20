package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerDTO {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @Pattern(regexp = "^0\\d{10}$")
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @Email(message = "The email address must be in the format of name@domain.com")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull @Pattern(regexp = "^0\\d{10}$") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull @Pattern(regexp = "^0\\d{10}$") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull @NotEmpty @Email(message = "The email address must be in the format of name@domain.com") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotEmpty @Email(message = "The email address must be in the format of name@domain.com") String email) {
        this.email = email;
    }
}
