package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.model.SeatCreateRequest;
import com.iyzico.challenge.model.SeatUpdateRequest;
import com.iyzico.challenge.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final FlightService flightService;

    public SeatService(SeatRepository seatRepository, FlightService flightService) {
        this.seatRepository = seatRepository;
        this.flightService = flightService;
    }

    public Seat getSeatById(Long seatId) {
        Optional<Seat> seat = seatRepository.findById(seatId);
        if (!seat.isPresent()) {
            throw new SeatException("api.error.seat.notFound.message");
        }

        return seat.get();
    }

    public void addSeatToFlight(Long flightId, SeatCreateRequest seatCreateRequest) {
        Flight flight = flightService.getFlightById(flightId);

        Seat seat = new Seat();
        seat.setFlight(flight);
        seat.setSeatNumber(seatCreateRequest.getSeatNumber());
        seat.setBooked(seatCreateRequest.getBooked());

        seatRepository.save(seat);
    }

    public void updateSeatById(Long flightId, Long seatId, SeatUpdateRequest seatUpdateRequest) {
        Seat seat = getSeatById(seatId);

        seat.setSeatNumber(seatUpdateRequest.getSeatNumber());
        seat.setBooked(seatUpdateRequest.getBooked());

        seatRepository.save(seat);
    }

    public void removeSeatById(Long flightId, Long seatId) {
        seatRepository.deleteById(seatId);
    }

}
