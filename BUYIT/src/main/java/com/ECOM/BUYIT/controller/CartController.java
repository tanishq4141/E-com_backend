package com.ECOM.BUYIT.controller;

import com.ECOM.BUYIT.dto.AddToCartRequest;
import com.ECOM.BUYIT.dto.CartItemResponse;
import com.ECOM.BUYIT.model.CartItem;
import com.ECOM.BUYIT.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping("/add")
  public ResponseEntity<CartItem> addToCart(@Valid @RequestBody AddToCartRequest request) {
    CartItem cartItem = cartService.addToCart(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable String userId) {
    List<CartItemResponse> cartItems = cartService.getCartItems(userId);
    return ResponseEntity.ok(cartItems);
  }

  @DeleteMapping("/{userId}/clear")
  public ResponseEntity<Map<String, String>> clearCart(@PathVariable String userId) {
    cartService.clearCart(userId);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Cart cleared successfully");
    return ResponseEntity.ok(response);
  }
}
