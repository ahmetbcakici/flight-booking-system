package com.iyzico.challenge.model;

import javax.validation.constraints.NotNull;

public class SeatCreateRequest {
    @NotNull(message = "api.validation.constraints.seatBooked.notNull.message")
    private Boolean isBooked;

    @NotNull(message = "api.validation.constraints.seatNumber.notNull.message")
    private String seatNumber;

    public Boolean getBooked() {
        return isBooked;
    }

    public void setBooked(Boolean booked) {
        isBooked = booked;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
