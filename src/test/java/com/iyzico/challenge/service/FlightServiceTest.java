package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.model.FlightCreateRequest;
import com.iyzico.challenge.model.FlightResponseDTO;
import com.iyzico.challenge.model.FlightUpdateRequest;
import com.iyzico.challenge.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlightById_shouldReturnFlight_whenFlightExists() {
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlightById(flightId);

        assertNotNull(result);
        assertEquals(flightId, result.getId());
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void getFlightById_shouldThrowException_whenFlightDoesNotExist() {
        Long flightId = 1L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> flightService.getFlightById(flightId));
        verify(flightRepository, times(1)).findById(flightId);
    }

    @Test
    void createFlight_shouldSaveFlight() {
        FlightCreateRequest request = new FlightCreateRequest();
        request.setName("Test Flight");
        request.setDescription("Test Description");
        request.setPrice(BigDecimal.valueOf(100));

        flightService.createFlight(request);

        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void updateFlightById_shouldUpdateFlight() {
        Long flightId = 1L;
        Flight existingFlight = new Flight();
        existingFlight.setId(flightId);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));

        FlightUpdateRequest request = new FlightUpdateRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setPrice(BigDecimal.valueOf(200));

        flightService.updateFlightById(flightId, request);

        verify(flightRepository, times(1)).save(existingFlight);
        assertEquals("Updated Name", existingFlight.getName());
        assertEquals("Updated Description", existingFlight.getDescription());
        assertEquals(BigDecimal.valueOf(200), existingFlight.getPrice());
    }

    @Test
    void removeFlightById_shouldDeleteFlight() {
        Long flightId = 1L;

        flightService.removeFlightById(flightId);

        verify(flightRepository, times(1)).deleteById(flightId);
    }

    @Test
    void getFlightWithSeats_shouldReturnFlightResponseDTO() {
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setName("Test Flight");
        flight.setDescription("Test Description");

        Seat seat1 = new Seat();
        seat1.setSeatNumber("A1");
        seat1.setBooked(true);

        Seat seat2 = new Seat();
        seat2.setSeatNumber("A2");
        seat2.setBooked(false);

        flight.setSeats(Arrays.asList(seat1, seat2));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        FlightResponseDTO result = flightService.getFlightWithSeats(flightId);

        assertNotNull(result);
        assertEquals(flightId, result.getId());
        assertEquals(2, result.getSeats().size());
        verify(flightRepository, times(1)).findById(flightId);
    }
}
