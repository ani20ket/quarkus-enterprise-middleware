package uk.ac.newcastle.enterprisemiddleware.taxi;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>Simple POJO representing Taxi objects</p>
 *
 */
public class TaxiBooking implements Serializable {

    private static final long serialVersionUID = 249872301293L;

    private Long id;
    private Customer customer;
    private Taxi taxi;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

