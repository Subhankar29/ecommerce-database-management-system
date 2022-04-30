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

import com.neu.dbms.model.CartProduct;
import com.neu.dbms.model.Category;
import com.neu.dbms.model.OrderDetail;
import com.neu.dbms.model.Orders;
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

  private Connection conn;

  /**
   * Get a new database connection
   * 
   * @return
   * @throws SQLException
   */
  public void getConnection() throws SQLException {
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
    this.conn = conn;
  }

  @Override
  public int getUser(String user_Name, String pass_word) {
    int res = 0;
    try {
      CallableStatement pstmt = this.conn.prepareCall("call get_User(?, ?)");
      pstmt.setString(1, user_Name);
      pstmt.setString(2, pass_word);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        res = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return res;
  }

  @Override
  public int getAccountId(String emailAddress) {
    try {
      CallableStatement pstmt = this.conn.prepareCall("call get_AccountId(?)");

      pstmt.setString(1, emailAddress);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
      return -1;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  @Override
  public List<Category> getCategories() {
    List<Category> categoryList = new ArrayList<>();
    try {
      CallableStatement catstmt = this.conn.prepareCall("call get_Categories()");
      ResultSet catrs = catstmt.executeQuery();
      while (catrs.next()) {
        Category cat = new Category();
        cat.setCategoryId(catrs.getInt(1));
        cat.setCategoryName(catrs.getString(2));
        cat.setDepartmentName(catrs.getString(3));
        cat.setDescription(catrs.getString(4));
        categoryList.add(cat);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return categoryList;
  }

  @Override
  public void addCart(int cartId, int productId, int quantity, int accountId) {

    try {
      String callQuery = "{CALL add_cart(?, ?, ?)}";
      CallableStatement stmt = this.conn.prepareCall(callQuery);

      stmt.setInt(1, productId);
      stmt.setInt(2, quantity);
      stmt.setInt(3, accountId);

      ResultSet resultSet = stmt.executeQuery();

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public int insertOrderDetails(int orderid, int userid) {
    int row = 0;
    try {
      CallableStatement callstmt = this.conn.prepareCall("call insert_Orders_Details(?, ?)");
      callstmt.setInt(1, orderid);
      callstmt.setInt(2, userid);
      ResultSet rs = callstmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return row;
  }

  @Override
  public void updateCart(int accountId, int productId, int quantity) {
    try {
      CallableStatement updateCart = this.conn.prepareCall("call update_cart(?, ?, ?)");
      updateCart.setInt(1, productId);
      updateCart.setInt(2, quantity);
      updateCart.setInt(3, accountId);
      updateCart.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public List<CartProduct> getProductsFromCart(int accountId) {
    List<CartProduct> productList = new ArrayList<>();
    try {
      CallableStatement prodpstmt = this.conn.prepareCall("call get_Products_From_Cart(?)");
      prodpstmt.setInt(1, accountId);
      ResultSet rs = prodpstmt.executeQuery();
      while (rs.next()) {
        CartProduct prod = new CartProduct();
        prod.setProductId(rs.getInt(1));
        prod.setName(rs.getString(2));
        prod.setQuantity(rs.getInt(3));
        prod.setSubtotal(rs.getFloat(4));
        productList.add(prod);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return productList;
  }

  @Override
  public int registerUser(String emailAddress, String password, String address, int contact,
      String state) {
    int res = 0;
    try {
      CallableStatement registerUser = this.conn.prepareCall("SELECT create_user(?, ?, ?, ?, ?)");
      registerUser.setString(1, password);
      registerUser.setString(2, emailAddress);
      registerUser.setString(3, address);
      registerUser.setInt(4, contact);
      registerUser.setString(5, state);

      ResultSet rs = registerUser.executeQuery();
      System.out.print("Successfully registered the users");

      if (rs.next()) {
        res = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return res;
  }

  @Override
  public List<Product> getProductsByCategory(int categoryId) {
    List<Product> productList = new ArrayList<>();
    try {
      CallableStatement prodpstmt = this.conn.prepareCall("call get_Products_By_Category(?)");
      prodpstmt.setInt(1, categoryId);
      ResultSet rs = prodpstmt.executeQuery();
      while (rs.next()) {
        Product prod = new Product();
        prod.setProductId(rs.getInt(1));
        prod.setName(rs.getString(2));
        prod.setDescription(rs.getString(3));
        prod.setPrice(rs.getFloat(4));
        prod.setSupplier(rs.getString(5));
        prod.setCategoryId(rs.getInt(6));
        productList.add(prod);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return productList;
  }

  @Override
  public int insertOrders(String shippingAddress, int userid, String status) {
    int res = 0;
    try {
      CallableStatement callstmt = this.conn.prepareCall("select insert_Orders(?,?,?)");
      callstmt.setString(1, shippingAddress);
      callstmt.setInt(2, userid);
      callstmt.setString(3, status);
      ResultSet rest = callstmt.executeQuery();
      if (rest.next()) {
        res = rest.getInt(1);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return res;
  }

  @Override
  public void insertPaymentInfo(int orderId, int paymentInfo, int accountId) {
    try {
      CallableStatement callstmt = this.conn.prepareCall("call insert_Payment_Info(?,?,?)");
      callstmt.setInt(1, orderId);
      callstmt.setInt(2, paymentInfo);
      callstmt.setInt(3, accountId);
      ResultSet rs = callstmt.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<String> getPaymentModes() {
    List<String> payModeList = new ArrayList<>();
    try {
      Statement paymode = this.conn.createStatement();
      ResultSet catrs = paymode.executeQuery("Select * from PaymentMode");
      while (catrs.next()) {
        payModeList.add(catrs.getString(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return payModeList;
  }

  public List<Orders> getOrdersByUser(int userId) {
    List<Orders> ordersList = new ArrayList<>();
    try {
      CallableStatement ordersstmt = this.conn.prepareCall("call get_Orders_By_User(?)");
      ordersstmt.setInt(1, userId);
      ResultSet rs = ordersstmt.executeQuery();
      while (rs.next()) {
        Orders od = new Orders();
        od.setOrderId(rs.getInt(1));
        od.setDateCreated(rs.getString(2));
        od.setTotal(rs.getInt(3));
        od.setStatus(rs.getString(4));
        ordersList.add(od);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ordersList;
  }

  public List<OrderDetail> getOrderDetails(int orderid) {
    List<OrderDetail> orderDetailList = new ArrayList<>();
    try {
      PreparedStatement ordersstmt = this.conn.prepareStatement("call get_Order_Details(?)");
      ordersstmt.setInt(1, orderid);
      ResultSet rs = ordersstmt.executeQuery();
      while (rs.next()) {
        OrderDetail od = new OrderDetail();
        od.setProductName(rs.getString(1));
        od.setQuantity(rs.getInt(2));
        od.setUnitCost(rs.getInt(3));
        od.setSubTotal(rs.getInt(4));
        od.setShippingAddress(rs.getString(5));
        od.setStatus(rs.getString(6));
        od.setPaymentMode(rs.getString(7));
        orderDetailList.add(od);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return orderDetailList;
  }

  public void cancelOrder(int orderid) {
    try {
      CallableStatement cancelorder = this.conn.prepareCall("call cancel_Order(?)");
      cancelorder.setInt(1, orderid);
      ResultSet rs = cancelorder.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public boolean ifUserExists(String email) {
    boolean ifUserExist = false;
    try {
      PreparedStatement ordersstmt = this.conn.prepareStatement("call if_User_Exists(?)");
      ordersstmt.setString(1, email);
      ResultSet rs = ordersstmt.executeQuery();
      if (rs.next()) {
        ifUserExist = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ifUserExist;
  }
}
