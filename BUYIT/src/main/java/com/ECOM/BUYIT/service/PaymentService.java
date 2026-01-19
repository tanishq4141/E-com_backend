package com.ECOM.BUYIT.service;

import com.ECOM.BUYIT.dto.PaymentRequest;
import com.ECOM.BUYIT.model.Order;
import com.ECOM.BUYIT.model.Payment;
import com.ECOM.BUYIT.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderService orderService;
  private final PaymentProcessor paymentProcessor;

  public Payment createPayment(PaymentRequest request) {
    Order order = orderService.getOrderById(request.getOrderId());

    if (!"CREATED".equals(order.getStatus())) {
      throw new RuntimeException("Order is not in CREATED status. Current status: " + order.getStatus());
    }

    Payment payment = new Payment();
    payment.setOrderId(request.getOrderId());
    payment.setAmount(request.getAmount());
    payment.setStatus("PENDING");
    payment.setPaymentId("pay_" + UUID.randomUUID().toString());
    payment.setCreatedAt(Instant.now());

    Payment savedPayment = paymentRepository.save(payment);
    paymentProcessor.processPayment(savedPayment.getId(), request.getOrderId());
    return savedPayment;
  }

  public void updatePaymentStatus(String orderId, String paymentId, String status) {
    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));

    payment.setStatus(status);
    payment.setPaymentId(paymentId);
    paymentRepository.save(payment);

    if ("SUCCESS".equals(status)) {
      orderService.updateOrderStatus(orderId, "PAID");
    } else if ("FAILED".equals(status)) {
      orderService.updateOrderStatus(orderId, "FAILED");
    }
  }

  public Payment getPaymentByOrderId(String orderId) {
    return paymentRepository.findByOrderId(orderId)
        .orElse(null);
  }
}
