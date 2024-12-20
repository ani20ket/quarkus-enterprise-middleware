package uk.ac.newcastle.enterprisemiddleware.customer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.hotelbooking.HotelBooking;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This is a Domain object. The Customer class represents how Customer resources are represented in the application
 * database.
 *
 * The class also specifies how a contacts are retrieved from the database (with @NamedQueries), and acceptable values
 * for Customer fields (with @NotNull, @Pattern etc...)
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Customer.FIND_ALL, query = "SELECT c FROM Customer c ORDER BY c.name ASC"),
        @NamedQuery(name = Customer.FIND_BY_EMAIL, query = "SELECT c FROM Customer c WHERE c.email =:email")
})
@XmlRootElement
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Customer {

    public static final String FIND_ALL = "Customer.findAll";
    public static final String FIND_BY_EMAIL = "Customer.findByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials")
    @Column(name = "name")
    private String name;

    @NotNull
    @Pattern(regexp = "^0\\d{10}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @NotEmpty
    @Email(message = "The email address must be in the format of name@domain.com")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<HotelBooking> hotelBookings;

    public @NotNull @Size(min = 1, max = 25) @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials") String getName() {
        return name;
    }

    public void setName(@NotNull @Size(min = 1, max = 25) @Pattern(regexp = "[A-Za-z-']+", message = "Please use a name without numbers or specials") String name) {
        this.name = name;
    }

    public @NotNull @Pattern(regexp = "^\\d{11}$$") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull @Pattern(regexp = "^\\d{11}$$") String phone) {
        this.phoneNumber = phone;
    }

    public @NotNull @NotEmpty @Email(message = "The email address must be in the format of name@domain.com") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotEmpty @Email(message = "The email address must be in the format of name@domain.com") String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
