package com.neu.dbms.model;

import org.springframework.stereotype.Component;

@Component
public class Product {
  
 private String name; 
 private String description; 
 private float price;
 private String supplier; 
 private int categoryId; 
 private int productId; 
 
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
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public float getPrice() {
    return price;
  }
  public void setPrice(float price) {
    this.price = price;
  }
  public String getSupplier() {
    return supplier;
  }
  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }
  public int getCategoryId() {
    return categoryId;
  }
  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

}
