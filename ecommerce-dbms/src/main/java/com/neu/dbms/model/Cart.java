package com.neu.dbms.model;

public class Cart {
  private int cartId; 
  private int quantity; 
  private String dateAdded;
  private int accountId; 
  private int productId;
  
  public int getCartId() {
    return cartId;
  }
  public void setCartId(int cartId) {
    this.cartId = cartId;
  }
  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  public String getDateAdded() {
    return dateAdded;
  }
  public void setDateAdded(String dateAdded) {
    this.dateAdded = dateAdded;
  }
  public int getAccountId() {
    return accountId;
  }
  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }
  public int getProductId() {
    return productId;
  }
  public void setProductId(int productId) {
    this.productId = productId;
  }
}
