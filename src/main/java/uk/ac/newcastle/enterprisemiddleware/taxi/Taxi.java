package uk.ac.newcastle.enterprisemiddleware.taxi;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Taxi {
    private int id;
    @JsonIgnore
    private String registration;
    private long numberOfSeats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public long getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(long numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
