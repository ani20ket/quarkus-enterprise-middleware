package uk.ac.newcastle.enterprisemiddleware.hotel;



import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represents a hotel entity in the system. This class is mapped to the "hotel" table in the
 * database and enforces a unique constraint on the phone number field. It includes named
 * queries to retrieve all hotels or search for hotels by phone number.
 *
 * <p>Named Queries:
 * <ul>
 *   <li>{@link #FIND_ALL}: Finds all hotels.</li>
 *   <li>{@link #FIND_ALL_BY_PHONE_NUMBER}: Finds hotels by phone number.</li>
 * </ul>
 *
 * Annotations:
 * <ul>
 *   <li>@Entity - Specifies that this class is an entity and is mapped to a database table.</li>
 *   <li>@NamedQueries - Defines named queries for retrieving hotel records.</li>
 *   <li>@XmlRootElement - Specifies that instances of this class can be marshaled/unmarshaled to/from XML.</li>
 *   <li>@Table - Specifies the primary table for the entity and enforces a unique constraint on the phone number column.</li>
 * </ul>
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Hotel.FIND_ALL, query = "SELECT h FROM Hotel h"),
        @NamedQuery(name = Hotel.FIND_ALL_BY_PHONE_NUMBER, query = "SELECT h FROM Hotel h WHERE h.phoneNumber =: phoneNumber")
})
@XmlRootElement
@Table(name = "hotel", uniqueConstraints = @UniqueConstraint(columnNames = "phone_number"))
public class Hotel {

    public static final String FIND_ALL = "Hotel.findAll";

    public static final String FIND_ALL_BY_PHONE_NUMBER = "Hotel.findAllByPhoneNumber";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "hotel_name")
    private String hotelName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$")
    @Column(name = "post_code")
    private String postCode;

    @NotNull
    @Pattern(regexp = "^0\\d{10}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setHotelName(@NotNull @Size(min = 1, max = 50) String hotelName) {
        this.hotelName = hotelName;
    }

    public void setPostCode(@NotNull @Pattern(regexp = "^[a-zA-Z0-9]{6}$") String postCode) {
        this.postCode = postCode;
    }

    public void setPhoneNumber(@NotNull @Pattern(regexp = "^0\\d{10}$") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}