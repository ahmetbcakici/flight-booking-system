package com.iyzico.challenge.controller;

import com.iyzico.challenge.service.PaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights/{flightId}/seats/{seatId}/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void createPayment(@PathVariable Long flightId, @PathVariable Long seatId) {
        paymentService.createPayment(flightId, seatId);
    }
}
