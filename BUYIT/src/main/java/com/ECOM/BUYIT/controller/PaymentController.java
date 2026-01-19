package com.ECOM.BUYIT.controller;

import com.ECOM.BUYIT.dto.PaymentRequest;
import com.ECOM.BUYIT.model.Payment;
import com.ECOM.BUYIT.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @PostMapping("/create")
  public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequest request) {
    Payment payment = paymentService.createPayment(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(payment);
  }
}
