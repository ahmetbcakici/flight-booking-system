package com.iyzico.challenge.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FlightUpdateRequest {
    @NotNull(message = "api.validation.constraints.flightName.notNull.message")
    private String name;
    @NotNull(message = "api.validation.constraints.flightDescription.notNull.message")
    private String description;
    @NotNull(message = "api.validation.constraints.flightPrice.notNull.message")
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
