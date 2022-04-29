package com.neu.dbms.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.neu.dbms.model.Category;
import com.neu.dbms.model.Product;

@Component
public interface EcommerceAppDao {

  public String getUser(String user_Name, String pass_word);

  public List<Category> getCategories();

  public List<Product> getProductsByCatgory(int categoryId);
  
  public void addCart(int cartId, int productId, int quantity, int accountId);
}
