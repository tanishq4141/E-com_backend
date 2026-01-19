package com.ECOM.BUYIT.service;

import com.ECOM.BUYIT.model.Product;
import com.ECOM.BUYIT.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public Product createProduct(Product product) {
    return productRepository.save(product);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Product getProductById(String id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
  }
}
