package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.exception.FlightException;
import com.iyzico.challenge.model.FlightCreateRequest;
import com.iyzico.challenge.model.FlightResponseDTO;
import com.iyzico.challenge.model.FlightUpdateRequest;
import com.iyzico.challenge.model.SeatResponseDTO;
import com.iyzico.challenge.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Flight getFlightById(Long flightId) {
        Optional<Flight> flight = flightRepository.findById(flightId);
        if (!flight.isPresent()) {
            throw new FlightException("api.error.flight.notFound.message");
        }

        return flight.get();
    }

    public void createFlight(FlightCreateRequest flightCreateRequest) {
        Flight flight = new Flight();
        flight.setName(flightCreateRequest.getName());
        flight.setDescription(flightCreateRequest.getDescription());
        flight.setPrice(flightCreateRequest.getPrice());
        flightRepository.save(flight);
    }

    public void updateFlightById(Long flightId, FlightUpdateRequest flightUpdateRequest) {
        Flight flight = getFlightById(flightId);
        flight.setName(flightUpdateRequest.getName());
        flight.setDescription(flightUpdateRequest.getDescription());
        flight.setPrice(flightUpdateRequest.getPrice());
        flightRepository.save(flight);
    }

    public void removeFlightById(Long flightId) {
        flightRepository.deleteById(flightId);
    }

    public FlightResponseDTO getFlightWithSeats(Long flightId) {
        Flight flight = getFlightById(flightId);

        return mapToResponseDTO(flight);
    }

    private FlightResponseDTO mapToResponseDTO(Flight flight) {
        FlightResponseDTO responseDTO = new FlightResponseDTO();
        responseDTO.setId(flight.getId());
        responseDTO.setName(flight.getName());
        responseDTO.setDescription(flight.getDescription());
        responseDTO.setSeats(flight.getSeats().stream()
                .map(seat -> {
                    SeatResponseDTO seatDTO = new SeatResponseDTO();
                    seatDTO.setSeatNumber(seat.getSeatNumber());
                    seatDTO.setBooked(seat.getBooked());
                    return seatDTO;
                }).collect(Collectors.toList()));
        return responseDTO;
    }
}
