package com.iyzico.challenge.model;

import java.util.List;

public class FlightResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private List<SeatResponseDTO> seats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<SeatResponseDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponseDTO> seats) {
        this.seats = seats;
    }
}
