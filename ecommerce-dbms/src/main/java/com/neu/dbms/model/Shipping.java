package com.neu.dbms.model;

public class Shipping {
private int shippingId;
private String shippingType; 
private int shippingCost; 
private int shippingRegionId;

public int getShippingId() {
  return shippingId;
}
public void setShippingId(int shippingId) {
  this.shippingId = shippingId;
}
public String getShippingType() {
  return shippingType;
}
public void setShippingType(String shippingType) {
  this.shippingType = shippingType;
}
public int getShippingCost() {
  return shippingCost;
}
public void setShippingCost(int shippingCost) {
  this.shippingCost = shippingCost;
}
public int getShippingRegionId() {
  return shippingRegionId;
}
public void setShippingRegionId(int shippingRegionId) {
  this.shippingRegionId = shippingRegionId;
}
}
