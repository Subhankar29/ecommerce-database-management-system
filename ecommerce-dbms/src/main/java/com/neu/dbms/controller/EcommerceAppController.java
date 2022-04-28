package com.neu.dbms.controller;

import org.springframework.stereotype.Component;

@Component
public interface EcommerceAppController {

  public String getAllUsers(String user_name, String pass_word);

}
