package com.neu.dbms.model;

public class Orders {
  
  private int orderId;
  private String dateCreated; 
  private String shippingAddress; 
  private int customerId;
  private double total;
  private String status;
  private int productId; 
  private int shippingId; 
  private int orderDetailsId; 
  
  public int getOrderId() {
    return orderId;
  }
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  public String getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }
  public String getShippingAddress() {
    return shippingAddress;
  }
  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }
  public int getCustomerId() {
    return customerId;
  }
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }
  public double getTotal() {
    return total;
  }
  public void setTotal(double total) {
    this.total = total;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public int getProductId() {
    return productId;
  }
  public void setProductId(int productId) {
    this.productId = productId;
  }
  public int getShippingId() {
    return shippingId;
  }
  public void setShippingId(int shippingId) {
    this.shippingId = shippingId;
  }
  public int getOrderDetailsId() {
    return orderDetailsId;
  }
  public void setOrderDetailsId(int orderDetailsId) {
    this.orderDetailsId = orderDetailsId;
  }
}
