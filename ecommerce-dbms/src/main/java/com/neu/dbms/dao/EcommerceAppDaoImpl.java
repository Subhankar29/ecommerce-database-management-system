package com.neu.dbms.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Repository;

import com.neu.dbms.model.Category;
import com.neu.dbms.model.Product;

@Repository
public class EcommerceAppDaoImpl implements EcommerceAppDao {
  /** The name of the MySQL account to use (or empty for anonymous) */
  private static String userName = "root";

  /** The password for the MySQL account (or empty for anonymous) */
  private static String password = "password";

  /** The name of the computer running MySQL */
  private final String serverName = "localhost";

  /** The port of the MySQL server (default is 3306) */
  private final int portNumber = 3306;

  /**
   * The name of the database we are testing with (this default is installed with
   * MySQL)
   */
  private final String dbName = "ecommerce";

  /** The name of the table we are testing with */
  private final String tableName = "lotr_character";
  private final boolean useSSL = false;
  private String inputcharname;
  private Connection conn;

  /**
   * Get a new database connection
   * 
   * @return
   * @throws SQLException
   */
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    Properties connectionProps = new Properties();
    try {
      connectionProps.put("user", userName);
      connectionProps.put("password", password);

      Class.forName("com.mysql.cj.jdbc.Driver");

      conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber
          + "/" + this.dbName + "?characterEncoding=UTF-8&useSSL=false", connectionProps);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return conn;
  }

  @Override
  public String getUser(String user_Name, String pass_word) {
    try {
      conn = this.getConnection();
      PreparedStatement pstmt = conn
          .prepareStatement("SELECT Email from user where Email = ? AND password = ?");
      List<String> characternames = new ArrayList<>();
      pstmt.setString(1, user_Name);
      pstmt.setString(2, pass_word);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return "User Exists";
      }
      return "Invalid username";
    } catch (SQLException e) {
      e.printStackTrace();
      return "Invalid username";
    }
  }

  @Override
  public List<Category> getCategories() {
    List<Category> categoryList = new ArrayList<>();
    try {
      Statement catstmt = conn.createStatement();
      ResultSet catrs = catstmt.executeQuery("Select * from Category");
      if (catrs.next()) {
        Category cat = new Category();
        cat.setCategoryId(catrs.getInt(0));
        cat.setCategoryName(catrs.getString(1));
        cat.setDepartmentName(catrs.getString(2));
        cat.setDescription(catrs.getString(3));
        categoryList.add(cat);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categoryList;
  }

  @Override
  public List<Product> getProductsByCatgory(int categoryId) {
    List<Product> productList = new ArrayList<>();
    try {
      Connection conn = this.getConnection();
      PreparedStatement prodpstmt = conn
          .prepareStatement("SELECT * from Products where CategoryId = ?");
      List<String> characternames = new ArrayList<>();
      prodpstmt.setInt(1, categoryId);
      ResultSet rs = prodpstmt.executeQuery();
      if (rs.next()) {
        Product prod = new Product();
        prod.setProductId(rs.getInt(0));
        prod.setDescription(rs.getNString(2));
        prod.setName(rs.getString(1));
        prod.setPrice(rs.getFloat(3));
        prod.setSupplier(rs.getString(4));
        prod.setCategoryId(rs.getInt(5));
        productList.add(prod);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return productList;
  }

//  // on click add to cart
//  @Override
//  public String addProducttoCart() {
//    try {
//      Connection conn = this.getConnection();
//      Statement pstmt = conn.createStatement();
//          .prepareStatement("Insert query for cart");
//      List<String> characternames = new ArrayList<>();
//
//      ResultSet rs = pstmt.executeQuery();
//      if (rs.next()) {
//        return "User Exists";
//      }
//      return "Invalid username";
//    } catch (SQLException e) {
//      e.printStackTrace();
//      return "Invalid username";
//    }
//  }
//
//  // on click of hovering back from cart
//
//  // call display all categories
//
//  // again on choosing a category it is going to display the products
//
//  // to back from prodcuts it displays the cateogories
//
//  @Override
//  public Sting oncartCheckout() {
//
//  }
//
//  // itll ask us to enter delivery address
//
//  // make changes in cart
//
//  public String update cartt
//
//  public String delete
//  items in
//
//  cart()
//
//  public String user
//
//  registration() {
//    insert queries for user
//  }
//
//  public String cancel
//
//  order() {
//    // delete order information
//    // delete payment information
//  }
//
//  public String cancel
//
//  order() {
//    // delete order information
//    // delete payment information
//  }
//
//  place order() {
//    // insert tuples in payment table
//    // shipping table
//    // add ctable
//  }
//
//  modify order() {
//    // you can modify the order within 24 hours of placing it.
//    // change the shipping address
//    // change the mode of payment.
//    // change the orderdetails
//    // insufficient funds on hold.
//  }
//
//  administrator(){
//    has option to modify the shipping information
//    change password.
//    updating the cost of the product
//    
//  }
//
//  promotions on
//
//  the category() {
//    // join promotions with category and update the order total field
//  }
}