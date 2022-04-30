package com.neu.dbms.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.neu.dbms.model.Category;

@Component
public interface EcommerceAppController {

  public String getUser(String user_name, String pass_word);

  public List<Category> getCategories();

}
