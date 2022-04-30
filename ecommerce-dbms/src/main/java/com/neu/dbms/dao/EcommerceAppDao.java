package com.neu.dbms.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.neu.dbms.model.CartProduct;
import com.neu.dbms.model.Category;
import com.neu.dbms.model.Product;

@Component
public interface EcommerceAppDao {

  public void updateCart(int accountId, int productId, int quantity);
  
  public int getUser(String emailAddress, String password);
  
  public List<CartProduct> getProductsFromCart(int accountid);
  
  public int getAccountId(String emailAddress);
  
  public int registerUser(String emailAddress, String password, String address, int contact, String state);

  public List<Category> getCategories();
  
  public void addCart(int cartId, int productId, int quantity, int accountId);
  
  public List<Product> getProductsByCategory(int categoryId);
  
  public int insertOrders(String shippingAddress, int userid, String status);

  public void insertPaymentInfo(int orderId, int paymentInfo, int accountId);

  public int insertOrderDetails(int orderid, int userid);
}
