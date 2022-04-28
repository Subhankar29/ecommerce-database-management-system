package com.neu.dbms.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface EcommerceAppDao {

  public String getAllProducts(String user_Name, String pass_word);

}
