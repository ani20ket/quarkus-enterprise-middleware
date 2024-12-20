package uk.ac.newcastle.enterprisemiddleware.hotelbooking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;


/**
 * Entity representing a hotel booking in the system.
 * Stores information related to a booking, including customer and associated hotel details.
 *
 * <p>This entity supports querying bookings by customer ID using the named query
 * {@link HotelBooking#FIND_ALL_BY_CUSTOMER_ID}. The table for this entity is defined as
 * "hotel_booking".</p>
 *
 * @see Hotel
 * @see Customer
 */

@Entity
@NamedQueries({
        @NamedQuery(name = HotelBooking.FIND_ALL_BY_CUSTOMER_ID, query = "SELECT h FROM HotelBooking h WHERE h.customer.id = :customerId"),
        @NamedQuery(name = HotelBooking.FIND_BY_HOTEL_ID_AND_DATE,
                query = "SELECT h FROM HotelBooking h WHERE h.hotelId = :hotelId AND h.checkInDate = :date")
})
@XmlRootElement
@Table(name = "hotel_booking")
public class HotelBooking {

    public static final String FIND_ALL_BY_CUSTOMER_ID = "HotelBooking.findAllByCustomerId";
    public static final String FIND_BY_HOTEL_ID_AND_DATE = "HotelBooking.findByHotelIdAndDate";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "hotel_id")

    private String hotelId;

    @NotNull
    @Column(name = "check_in_date")
    @Future(message = "The check in date must be future")
    private LocalDate checkInDate;

    @NotNull
    @Column(name = "check_out_date")
    @Future(message = "The check out date must be future")
    private LocalDate checkOutDate;

    @NotNull
    @Column(name = "no_of_guests")
    private long noOfGuests;

    @NotNull
    @Column(name = "no_of_rooms")
    private long noOfRooms;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
