package com.iyzico.challenge.entity;

import javax.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue
    private Long id;
    private String seatNumber;
    private Boolean isBooked;
    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
