package com.neu.dbms.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public interface EcommerceAppService {
  
  public String getAllProducts(String user_Name, String pass_word);
  
}
