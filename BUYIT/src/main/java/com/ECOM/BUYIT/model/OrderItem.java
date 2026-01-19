package com.ECOM.BUYIT.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

  private String id;

  private String productId;

  private String productName;

  private Integer quantity;

  private Double price;
}
