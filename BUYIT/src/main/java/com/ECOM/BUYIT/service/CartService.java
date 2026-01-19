package com.ECOM.BUYIT.service;

import com.ECOM.BUYIT.dto.AddToCartRequest;
import com.ECOM.BUYIT.dto.CartItemResponse;
import com.ECOM.BUYIT.model.CartItem;
import com.ECOM.BUYIT.model.Product;
import com.ECOM.BUYIT.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final ProductService productService;

  public CartItem addToCart(AddToCartRequest request) {
    Product product = productService.getProductById(request.getProductId());

    if (product.getStock() < request.getQuantity()) {
      throw new RuntimeException("Insufficient stock for product: " + product.getName());
    }
    Optional<CartItem> existingItem = cartItemRepository
        .findByUserIdAndProductId(request.getUserId(), request.getProductId());

    if (existingItem.isPresent()) {
      CartItem item = existingItem.get();
      int newQuantity = item.getQuantity() + request.getQuantity();

      if (product.getStock() < newQuantity) {
        throw new RuntimeException("Insufficient stock for product: " + product.getName());
      }

      item.setQuantity(newQuantity);
      return cartItemRepository.save(item);
    } else {
      CartItem newItem = new CartItem();
      newItem.setUserId(request.getUserId());
      newItem.setProductId(request.getProductId());
      newItem.setQuantity(request.getQuantity());
      return cartItemRepository.save(newItem);
    }
  }

  public List<CartItemResponse> getCartItems(String userId) {
    List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

    return cartItems.stream().map(cartItem -> {
      Product product = productService.getProductById(cartItem.getProductId());
      CartItemResponse response = new CartItemResponse();
      response.setId(cartItem.getId());
      response.setProductId(cartItem.getProductId());
      response.setQuantity(cartItem.getQuantity());
      response.setProduct(product);
      return response;
    }).collect(Collectors.toList());
  }

  @Transactional
  public void clearCart(String userId) {
    cartItemRepository.deleteByUserId(userId);
  }
}
