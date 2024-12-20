package uk.ac.newcastle.enterprisemiddleware.travelagent;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

/**
 * Entity class representing a Travel Agent booking record.
 * <p>
 * This class stores information about a travel agent's bookings, including associated hotel, taxi, and flight bookings.
 * It maps to the "travel_agent" table in the database and is used to retrieve or store information related to bookings
 * for a specific customer.
 * </p>
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgent.FIND_ALL_BY_CUSTOMER_ID, query = "SELECT t FROM TravelAgent t WHERE t.customerId = :customerId")
})
@XmlRootElement
@Table(name = "travel_agent")
public class TravelAgent {
    public static final String FIND_ALL_BY_CUSTOMER_ID = "TravelAgent.findAllByCustomerId";
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private Long hotelBookingId;
    private Long taxiBookingId;
    private Long flightBookingId;
    private LocalDate date;
    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelBookingId() {
        return hotelBookingId;
    }

    public void setHotelBookingId(Long hotelBookingId) {
        this.hotelBookingId = hotelBookingId;
    }

    public Long getTaxiBookingId() {
        return taxiBookingId;
    }

    public void setTaxiBookingId(Long taxiBookingId) {
        this.taxiBookingId = taxiBookingId;
    }

    public Long getFlightBookingId() {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId) {
        this.flightBookingId = flightBookingId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
