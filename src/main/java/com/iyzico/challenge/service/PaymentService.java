package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.SeatException;
import com.iyzico.challenge.model.BankPaymentRequest;
import com.iyzico.challenge.model.BankPaymentResponse;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final FlightService flightService;
    private final BankService bankService;
    private final SeatRepository seatRepository;

    public PaymentService(PaymentRepository paymentRepository, FlightService flightService, BankService bankService, SeatRepository seatRepository) {
        this.paymentRepository = paymentRepository;
        this.flightService = flightService;
        this.bankService = bankService;
        this.seatRepository = seatRepository;
    }

    public void createPayment(Long flightId, Long seatId) {
        Flight flight = flightService.getFlightById(flightId);

        Seat seat = seatRepository.findByIdWithLock(seatId)
                .orElseThrow(() -> new SeatException("api.error.seat.notFound.message"));


        if (!flight.getId().equals(seat.getFlight().getId())) {
            logger.error("Seat {} does not belong to flight {}", seatId, flightId);
            throw new SeatException("api.error.seat.notBelongToTheFlight.message");
        }

        if (seat.getBooked()) {
            logger.error("Seat {} is already booked", seatId);
            throw new SeatException("api.error.seat.alreadyBooked.message");
        }

        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(flight.getPrice());
        BankPaymentResponse response = bankService.pay(request);

        seat.setBooked(true);
        seatRepository.save(seat);

        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(flight.getPrice());
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");
    }

}


