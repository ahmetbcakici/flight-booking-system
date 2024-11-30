package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.SeatCreateRequest;
import com.iyzico.challenge.model.SeatUpdateRequest;
import com.iyzico.challenge.service.SeatService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/flights/{flightId}/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public void addSeatToFlight(@PathVariable Long flightId, @RequestBody @Valid SeatCreateRequest seatCreateRequest) {
        seatService.addSeatToFlight(flightId, seatCreateRequest);
    }

    @PutMapping("/{seatId}")
    public void updateSeatById(@PathVariable Long flightId, @PathVariable Long seatId, @RequestBody @Valid SeatUpdateRequest seatUpdateRequest) {
        seatService.updateSeatById(flightId, seatId, seatUpdateRequest);
    }

    @DeleteMapping("/{seatId}")
    public void removeSeatById(@PathVariable Long flightId, @PathVariable Long seatId) {
        seatService.removeSeatById(flightId, seatId);
    }
}
