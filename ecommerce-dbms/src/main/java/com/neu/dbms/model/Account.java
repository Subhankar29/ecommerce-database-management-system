package com.neu.dbms.model;

public class Account {
  
  private String billing_address; 
  private boolean isClosed; 
  private float openDate; 
  private float closeDate; 
  private int orderId; 
  private int userId; 
  private int accountId;
  
  public int getAccountId() {
    return accountId;
  }
  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }
  public String getBilling_address() {
    return billing_address;
  }
  public void setBilling_address(String billing_address) {
    this.billing_address = billing_address;
  }
  public boolean isClosed() {
    return isClosed;
  }
  public void setClosed(boolean isClosed) {
    this.isClosed = isClosed;
  }
  public float getOpenDate() {
    return openDate;
  }
  public void setOpenDate(float openDate) {
    this.openDate = openDate;
  }
  public float getCloseDate() {
    return closeDate;
  }
  public void setCloseDate(float closeDate) {
    this.closeDate = closeDate;
  }
  public int getOrderId() {
    return orderId;
  }
  public void setOrderId(int orderId) {
    this.orderId = orderId;
  }
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }

}
