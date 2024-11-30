package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.model.BankPaymentRequest;
import com.iyzico.challenge.model.BankPaymentResponse;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private FlightService flightService;
    private BankService bankService;
    private SeatRepository seatRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        flightService = mock(FlightService.class);
        bankService = mock(BankService.class);
        seatRepository = mock(SeatRepository.class);

        paymentService = new PaymentService(paymentRepository, flightService, bankService, seatRepository);
    }

    @Test
    void testCreatePayment_SuccessfulPayment() {
        Long flightId = 1L;
        Long seatId = 2L;

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);
        mockFlight.setPrice(BigDecimal.valueOf(150.00));

        Seat mockSeat = new Seat();
        mockSeat.setId(seatId);
        mockSeat.setFlight(mockFlight);
        mockSeat.setBooked(false);

        BankPaymentRequest mockRequest = new BankPaymentRequest();
        mockRequest.setPrice(BigDecimal.valueOf(150.00));

        BankPaymentResponse mockResponse = new BankPaymentResponse("SUCCESS");

        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);
        when(seatRepository.findByIdWithLock(seatId)).thenReturn(Optional.of(mockSeat));
        when(bankService.pay(any(BankPaymentRequest.class))).thenReturn(mockResponse);

        paymentService.createPayment(flightId, seatId);

        assertTrue(mockSeat.getBooked());
        verify(seatRepository, times(1)).save(mockSeat);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(BigDecimal.valueOf(150.00), capturedPayment.getPrice());
        assertEquals("SUCCESS", capturedPayment.getBankResponse());

        ArgumentCaptor<BankPaymentRequest> requestCaptor = ArgumentCaptor.forClass(BankPaymentRequest.class);
        verify(bankService, times(1)).pay(requestCaptor.capture());
        BankPaymentRequest capturedRequest = requestCaptor.getValue();
        assertEquals(BigDecimal.valueOf(150.00), capturedRequest.getPrice());
    }

    @Test
    void testCreatePayment_SeatNotFound_ShouldThrowException() {
        Long flightId = 1L;
        Long seatId = 2L;

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);

        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);
        when(seatRepository.findByIdWithLock(seatId)).thenReturn(Optional.empty());

        SeatException exception = assertThrows(SeatException.class, () -> paymentService.createPayment(flightId, seatId));
        assertEquals("api.error.seat.notFound.message", exception.getMessage());
    }

    @Test
    void testCreatePayment_SeatAlreadyBooked_ShouldThrowException() {
        Long flightId = 1L;
        Long seatId = 2L;

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);

        Seat mockSeat = new Seat();
        mockSeat.setId(seatId);
        mockSeat.setFlight(mockFlight);
        mockSeat.setBooked(true);

        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);
        when(seatRepository.findByIdWithLock(seatId)).thenReturn(Optional.of(mockSeat));

        SeatException exception = assertThrows(SeatException.class, () -> paymentService.createPayment(flightId, seatId));
        assertEquals("api.error.seat.alreadyBooked.message", exception.getMessage());
    }

    @Test
    void testCreatePayment_SeatNotBelongToFlight_ShouldThrowException() {
        Long flightId = 1L;
        Long seatId = 2L;

        Flight mockFlight = new Flight();
        mockFlight.setId(flightId);

        Flight differentFlight = new Flight();
        differentFlight.setId(3L);

        Seat mockSeat = new Seat();
        mockSeat.setId(seatId);
        mockSeat.setFlight(differentFlight);

        when(flightService.getFlightById(flightId)).thenReturn(mockFlight);
        when(seatRepository.findByIdWithLock(seatId)).thenReturn(Optional.of(mockSeat));

        SeatException exception = assertThrows(SeatException.class, () -> paymentService.createPayment(flightId, seatId));
        assertEquals("api.error.seat.notBelongToTheFlight.message", exception.getMessage());
    }
}