package com.neu.dbms.model;

public class OrderDetail {
  private String ProductName;
  private int quantity;
  private float unitCost;
  private float subTotal;
  private String shippingAddress;
  private String status;
  private String paymentMode;

  public String getProductName() {
    return ProductName;
  }

  public void setProductName(String productName) {
    ProductName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public float getUnitCost() {
    return unitCost;
  }

  public void setUnitCost(float unitCost) {
    this.unitCost = unitCost;
  }

  public float getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(float subTotal) {
    this.subTotal = subTotal;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }

}
