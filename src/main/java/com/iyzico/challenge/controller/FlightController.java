package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.FlightCreateRequest;
import com.iyzico.challenge.model.FlightResponseDTO;
import com.iyzico.challenge.model.FlightUpdateRequest;
import com.iyzico.challenge.service.FlightService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{flightId}")
    public FlightResponseDTO getFlightWithSeats(@PathVariable Long flightId) {
        return flightService.getFlightWithSeats(flightId);
    }

    @PostMapping
    public void createFlight(@RequestBody @Valid FlightCreateRequest flightCreateRequest) {
        flightService.createFlight(flightCreateRequest);
    }

    @PutMapping("/{flightId}")
    public void updateFlight(@PathVariable Long flightId, @RequestBody @Valid FlightUpdateRequest flightUpdateRequest) {
        flightService.updateFlightById(flightId, flightUpdateRequest);
    }

    @DeleteMapping("/{flightId}")
    public void removeFlightById(@PathVariable Long flightId) {
        flightService.removeFlightById(flightId);
    }
}
