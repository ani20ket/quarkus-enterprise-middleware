package uk.ac.newcastle.enterprisemiddleware.taxi;

import java.time.LocalDate;

public class TaxiDTO {
    private int taxiId;
    private LocalDate date;

    public int getTaxiId() {
        return taxiId;
    }

    public void setTaxiId(int taxiId) {
        this.taxiId = taxiId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
