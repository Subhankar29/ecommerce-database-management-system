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
  public String getAllProducts(String user_Name, String pass_word) {
    try {
      Connection conn = this.getConnection();
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
}
