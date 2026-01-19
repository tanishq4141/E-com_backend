package com.ECOM.BUYIT.webhook;

import com.ECOM.BUYIT.dto.PaymentWebhookRequest;
import com.ECOM.BUYIT.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

  private final PaymentService paymentService;

  @PostMapping("/payment")
  public ResponseEntity<Map<String, String>> handlePaymentWebhook(@RequestBody PaymentWebhookRequest request) {
    log.info("Received payment webhook for order: {}, status: {}", request.getOrderId(), request.getStatus());

    try {
      paymentService.updatePaymentStatus(
          request.getOrderId(),
          request.getPaymentId(),
          request.getStatus());

      Map<String, String> response = new HashMap<>();
      response.put("message", "Webhook processed successfully");
      response.put("orderId", request.getOrderId());
      response.put("status", request.getStatus());

      log.info("Webhook processed successfully for order: {}", request.getOrderId());

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      log.error("Error processing webhook for order: {}", request.getOrderId(), e);
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", e.getMessage());
      return ResponseEntity.badRequest().body(errorResponse);
    }
  }
}
