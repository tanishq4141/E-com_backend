package com.ECOM.BUYIT.service;

import com.ECOM.BUYIT.dto.CreateOrderRequest;
import com.ECOM.BUYIT.model.*;
import com.ECOM.BUYIT.repository.CartItemRepository;
import com.ECOM.BUYIT.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductService productService;

  @Transactional
  public Order createOrder(CreateOrderRequest request) {
    List<CartItem> cartItems = cartItemRepository.findByUserId(request.getUserId());

    if (cartItems.isEmpty()) {
      throw new RuntimeException("Cart is empty. Cannot create order.");
    }
    Order order = new Order();
    order.setUserId(request.getUserId());
    order.setStatus("CREATED");
    order.setCreatedAt(Instant.now());

    List<OrderItem> orderItems = new ArrayList<>();
    double totalAmount = 0.0;
    for (CartItem cartItem : cartItems) {
      Product product = productService.getProductById(cartItem.getProductId());
      if (product.getStock() < cartItem.getQuantity()) {
        throw new RuntimeException("Insufficient stock for product: " + product.getName());
      }
      OrderItem orderItem = new OrderItem();
      orderItem.setId(UUID.randomUUID().toString());
      orderItem.setProductId(product.getId());
      orderItem.setProductName(product.getName());
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setPrice(product.getPrice());
      orderItems.add(orderItem);
      totalAmount += product.getPrice() * cartItem.getQuantity();
      product.setStock(product.getStock() - cartItem.getQuantity());
      productService.createProduct(product);
    }

    order.setItems(orderItems);
    order.setTotalAmount(totalAmount);
    Order savedOrder = orderRepository.save(order);
    cartItemRepository.deleteByUserId(request.getUserId());

    return savedOrder;
  }

  public Order getOrderById(String orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
  }

  public void updateOrderStatus(String orderId, String status) {
    Order order = getOrderById(orderId);
    order.setStatus(status);
    orderRepository.save(order);
  }
}
