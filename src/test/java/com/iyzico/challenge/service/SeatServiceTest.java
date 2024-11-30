package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.model.SeatCreateRequest;
import com.iyzico.challenge.model.SeatUpdateRequest;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeatServiceTest {

    private SeatRepository seatRepository;
    private FlightService flightService;
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        seatRepository = mock(SeatRepository.class);
        flightService = mock(FlightService.class);
        seatService = new SeatService(seatRepository, flightService);
    }

    @Test
    void testGetSeatById_WhenSeatExists_ShouldReturnSeat() {
        Long seatId = 1L;
        Seat mockSeat = new Seat();
        mockSeat.setId(seatId);
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(mockSeat));

        Seat result = seatService.getSeatById(seatId);

        assertNotNull(result);
        assertEquals(seatId, result.getId());
        verify(seatRepository, times(1)).findById(seatId);
    }

    @Test
    void testGetSeatById_WhenSeatDoesNotExist_ShouldThrowException() {
        Long seatId = 1L;
        when(seatRepository.findById(seatId)).thenReturn(Optional.empty());

        assertThrows(SeatException.class, () -> seatService.getSeatById(seatId));
        verify(seatRepository, times(1)).findById(seatId);
    }

    @Test
    void testAddSeatToFlight_ShouldSaveSeat() {
        Long flightId = 1L;
        SeatCreateRequest seatCreateRequest = new SeatCreateRequest();
        seatCreateRequest.setSeatNumber("A1");
        seatCreateRequest.setBooked(false);

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);
        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);

        seatService.addSeatToFlight(flightId, seatCreateRequest);

        ArgumentCaptor<Seat> seatCaptor = ArgumentCaptor.forClass(Seat.class);
        verify(seatRepository, times(1)).save(seatCaptor.capture());
        Seat capturedSeat = seatCaptor.getValue();
        assertEquals(mockFlight, capturedSeat.getFlight());
        assertEquals("A1", capturedSeat.getSeatNumber());
        assertFalse(capturedSeat.getBooked());
    }

    @Test
    void testUpdateSeatById_ShouldUpdateSeat() {
        Long seatId = 1L;
        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest();
        seatUpdateRequest.setSeatNumber("B2");
        seatUpdateRequest.setBooked(true);

        Seat mockSeat = new Seat();
        mockSeat.setId(seatId);
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(mockSeat));

        seatService.updateSeatById(1L, seatId, seatUpdateRequest);

        verify(seatRepository, times(1)).save(mockSeat);
        assertEquals("B2", mockSeat.getSeatNumber());
        assertTrue(mockSeat.getBooked());
    }

    @Test
    void testUpdateSeatById_WhenSeatDoesNotExist_ShouldThrowException() {
        Long seatId = 1L;
        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest();
        when(seatRepository.findById(seatId)).thenReturn(Optional.empty());

        assertThrows(SeatException.class, () -> seatService.updateSeatById(1L, seatId, seatUpdateRequest));
        verify(seatRepository, never()).save(any());
    }

    @Test
    void testRemoveSeatById_ShouldDeleteSeat() {
        Long seatId = 1L;

        seatService.removeSeatById(1L, seatId);

        verify(seatRepository, times(1)).deleteById(seatId);
    }
}