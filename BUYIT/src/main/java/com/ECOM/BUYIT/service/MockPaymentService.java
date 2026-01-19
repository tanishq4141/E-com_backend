package com.ECOM.BUYIT.service;

import com.ECOM.BUYIT.dto.PaymentWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockPaymentService {

  private final RestTemplate restTemplate;

  @Value("${app.webhook.base-url}")
  private String webhookBaseUrl;

  @Async
  public void processPayment(String paymentId, String orderId) {
    try {
      log.info("Processing payment for order: {}", orderId);
      Thread.sleep(3000);
      String status = "SUCCESS";

      log.info("Payment successful for order: {}. Calling webhook...", orderId);
      PaymentWebhookRequest webhookRequest = new PaymentWebhookRequest();
      webhookRequest.setOrderId(orderId);
      webhookRequest.setPaymentId(paymentId);
      webhookRequest.setStatus(status);

      String webhookUrl = webhookBaseUrl + "/api/webhooks/payment";

      restTemplate.postForEntity(webhookUrl, webhookRequest, String.class);

      log.info("Webhook called successfully for order: {}", orderId);

    } catch (InterruptedException e) {
      log.error("Payment processing interrupted for order: {}", orderId);
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      log.error("Error processing payment for order: {}", orderId, e);
    }
  }
}
