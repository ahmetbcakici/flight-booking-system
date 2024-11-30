package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.model.BankPaymentRequest;
import com.iyzico.challenge.model.BankPaymentResponse;
import com.iyzico.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Transactional
public class IyzicoPaymentService {

    private Logger logger = LoggerFactory.getLogger(IyzicoPaymentService.class);

    private BankService bankService;
    private PaymentRepository paymentRepository;

    private final BlockingQueue<Payment> paymentQueue = new LinkedBlockingQueue<>();


    public IyzicoPaymentService(BankService bankService, PaymentRepository paymentRepository) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;

        new Thread(this::processPayments).start();
    }


    public void pay(BigDecimal price) {

        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);

        try {
            paymentQueue.put(payment);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to queue payment for processing", e);
        }
    }

    private void processPayments() {
        while (true) {
            try {
                Payment payment = paymentQueue.take();
                paymentRepository.save(payment);
                logger.info("Payment saved successfully!");
            } catch (Exception e) {
                logger.error("processPayments error : {}", e.getMessage());
            }
        }
    }
}