package com.ECOM.BUYIT.controller;

import com.ECOM.BUYIT.dto.CreateOrderRequest;
import com.ECOM.BUYIT.model.Order;
import com.ECOM.BUYIT.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
    Order order = orderService.createOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
    Order order = orderService.getOrderById(orderId);
    return ResponseEntity.ok(order);
  }
}
