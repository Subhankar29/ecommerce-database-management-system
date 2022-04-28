package com.neu.dbms.model;

public class User {
private int loginId;
private int password; 
private String email;
private String contact; 
private boolean ifLoggedIn;
private String varchar;

public int getLoginId() {
  return loginId;
}
public void setLoginId(int loginId) {
  this.loginId = loginId;
}
public int getPassword() {
  return password;
}
public void setPassword(int password) {
  this.password = password;
}
public String getEmail() {
  return email;
}
public void setEmail(String email) {
  this.email = email;
}
public String getContact() {
  return contact;
}
public void setContact(String contact) {
  this.contact = contact;
}
public boolean isIfLoggedIn() {
  return ifLoggedIn;
}
public void setIfLoggedIn(boolean ifLoggedIn) {
  this.ifLoggedIn = ifLoggedIn;
}
public String getVarchar() {
  return varchar;
}
public void setVarchar(String varchar) {
  this.varchar = varchar;
}

}
