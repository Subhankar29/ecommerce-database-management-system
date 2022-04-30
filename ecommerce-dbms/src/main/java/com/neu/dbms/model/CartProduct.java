package com.neu.dbms.model;

public class CartProduct {
  
  private int productId;
  private String name; 
  private int quantity;
  private float subtotal;
  
  public int getProductId() {
    return productId;
  }
  public void setProductId(int productId) {
    this.productId = productId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public float getSubtotal() {
    return subtotal;
  }
  public void setSubtotal(float subtotal) {
    this.subtotal = subtotal;
  }
}
