package com.neu.dbms.model;

public class OrderDetail {
private int orderDetailId; 
private int orderId; 
private String productName; 
private int quantity; 
private float unitCost; 
private float subTotal; 

public int getOrderDetailId() {
  return orderDetailId;
}
public void setOrderDetailId(int orderDetailId) {
  this.orderDetailId = orderDetailId;
}
public int getOrderId() {
  return orderId;
}
public void setOrderId(int orderId) {
  this.orderId = orderId;
}
public String getProductName() {
  return productName;
}
public void setProductName(String productName) {
  this.productName = productName;
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

}
