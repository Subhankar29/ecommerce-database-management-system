package com.neu.dbms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.neu.dbms.dao.EcommerceAppDaoImpl;
import com.neu.dbms.model.CartProduct;
import com.neu.dbms.model.Category;
import com.neu.dbms.model.OrderDetail;
import com.neu.dbms.model.Orders;
import com.neu.dbms.model.Product;

public class EcommerceAppController {

  private EcommerceAppDaoImpl ecommerceAppDao = new EcommerceAppDaoImpl();
  double totalVal = 0;
  int paymentIndex = 0;
  int userId = 0;
  int accountId = 0;

  public void startApplication() {
    this.establishConnection();

    Scanner in = new Scanner(System.in);

    String username = "";
    System.out.println("Welcome to Ecommerce Database\n\n");

    while (username.isBlank()) {
      System.out.println("Enter username");
      username = in.nextLine();
    }

    String password = "";

    while (password.isBlank()) {
      System.out.println("Enter Password");
      password = in.nextLine();
    }

    this.userId = this.getUser(username, password);

    if (this.userId == 0) {
      boolean isValidInput = false;
      while (!isValidInput) {
        System.out.println("Do you want to register user ? (y/n)");
        String ifregister = in.nextLine();
        if ("y".contentEquals(ifregister)) {
          registerNewUser(in);
          isValidInput = true;
          break;
        } else if ("n".contentEquals(ifregister)) {
          System.out.print("Operation stopped");
          in.close();
          isValidInput = true;
          break;
        }
      }
    }

    boolean isMainValidInput = false;

    while (!isMainValidInput) {
      System.out.println("----------------SELECT AN ACTION--------------");
      System.out.println("SELECT 1. TO SEARCH FOR A PRODUCT");
      System.out.println("SELECT 2. TO MANAGE ORDERS");
      System.out.println("SELECT 3. TO VIEW CART");
      System.out.println("SELECT 4. TO LOG OUT");

      boolean isValidAction = false;
      int action = 0;
      while (!isValidAction) {
        try {
          action = Integer.parseInt(in.nextLine());
          isValidAction = true;
          break;
        } catch (NumberFormatException ne) {
          System.out.println("Please enter valid number");
        }
      }

      accountId = ecommerceAppDao.getAccountId(username);

      if (action == 1) {
        addProducts(in);

        addToCart(in);

//        moveToShipping(in);
      } else if (action == 2) {
        manageOrders(in);
      } else if (action == 3) {
        addToCart(in);
        moveToShipping(in);
      }
      if (action == 4) {
        System.out.println("LOGGIN OUT");
        in.close();
        isValidAction = true;
        break;
      }
    }
  }

  public void manageOrders(Scanner in) {
    List<Orders> orderList = this.getOrdersByUser(this.userId);

    System.out.println(
        String.format("%-15s%-15s%-15s%-15s", "orderId", "date Created", "status", "total"));

    orderList.forEach(s -> {
      System.out.println(String.format("%-15s%-15s%-15s%-15s", s.getOrderId(), s.getDateCreated(),
          s.getStatus(), s.getTotal()));
    });

    System.out.println("Enter the orderId:");
    int orderid = Integer.parseInt(in.nextLine());

    List<OrderDetail> orderDetailList = this.getOrderDetails(orderid);

    System.out.println(String.format("%-15s%-15s%-15s%-15s%-15s%-25s", "ProductName", "UnitCost",
        "SubTotal", "Status", "PaymentMode", "Address"));

    orderDetailList.forEach(t -> {
      System.out.println(
          String.format("%-15s%-15s%-15s%-15s%-15s%-25s", t.getProductName(), t.getUnitCost(),
              t.getSubTotal(), t.getStatus(), t.getPaymentMode(), t.getShippingAddress()));
    });

    System.out.println("Please select if you want to cancel the order(y/n) ?");
    String ifCancel = in.nextLine();
    if ("y".equalsIgnoreCase(ifCancel)) {
      this.cancelOrder(orderid);
      System.out.println("Order canceled");
    }
  }

  public void moveToShipping(Scanner in) {
    System.out.println("----------PROCEED TO SHIPPING--------");
    System.out.println("Enter Shipping Address:");
    String shipAdd = in.nextLine();
    int orderid = this.insertOrders(shipAdd, this.userId, "Placed");
    this.insertOrderDetails(orderid, this.userId);
    System.out.println("Choose a mode of payment: (Enter the id)");
    List<String> modeofPayment = this.getPaymentModes();
    modeofPayment.forEach(s -> {
      paymentIndex++;
      System.out.println(paymentIndex + " ---> " + s);
    });
    int modeOfPayment = Integer.parseInt(in.nextLine());
    this.insertPaymentInfo(orderid, modeOfPayment, accountId);
    System.out.println("Order Placed:");
  }

  public void addToCart(Scanner in) {
    boolean isAddToCart = true;

    while (isAddToCart) {
      System.out.println("Products in cart:");
      List<CartProduct> productList = ecommerceAppDao.getProductsFromCart(accountId);

      totalVal = 0;

      System.out.println(
          String.format("%-15s%-15s%-15s%-15s", "Product Id", "Name", "Quantity", "Sub Total"));

      productList.forEach(product -> {
        System.out.println(String.format("%-15s%-15s%-15s%-15s", product.getProductId(),
            product.getName(), product.getQuantity(), product.getSubtotal()));

        totalVal = totalVal + product.getSubtotal();
      });

      System.out.println("Total cart amount: " + totalVal);

      boolean isCartValue = true;

      while (isCartValue) {
        System.out.println(
            "Do you want to update cart or go to checkout? Press Y to update cart or N to checkout");

        String isUpdateCart = in.nextLine();

        if ("y".contentEquals(isUpdateCart)) {
          try {
            System.out.println("Select product id to update the quantity");
            int productId = in.nextInt();
            System.out.println("Please sepcify the product quantity");
            int quantity = in.nextInt();
            ecommerceAppDao.updateCart(accountId, productId, quantity);
            isUpdateCart = "";
          } catch (Exception e) {
            System.out.println("Execution failed please try again");
            continue;
          }
        } else {
          isAddToCart = false;
          isCartValue = true;
          moveToShipping(in);
          break;
        }
      }

    }
  }

  public void addProducts(Scanner in) {
    boolean isContinueToAddProduct = true;

    while (isContinueToAddProduct) {
      List<Category> categoryList = this.getCategories();
      System.out.println("\n");

      System.out.println(String.format("%-15s%-15s", "Category Id", "Category Name"));

      categoryList.forEach(s -> {
        System.out.println(String.format("%-15s%-15s", s.getCategoryId(), s.getCategoryName()));
      });
      System.out.println("\nChoose a category (Enter the id)");
      int selectedCategory = Integer.parseInt(in.nextLine());
      List<Product> productList = this.getProductsByCatgory(selectedCategory);

      System.out.println(
          String.format("%-30s%-30s%-30s%-30s", "Product Id", "Name", "Price", "Description"));

      productList.forEach(product -> {
        System.out.println(String.format("%-30s%-30s%-30s%-30s", product.getProductId(),
            product.getName(), product.getPrice(), product.getDescription()));
      });

      System.out.println("Select a product (product Id) to add to cart");
      int productId = Integer.parseInt(in.nextLine());

      try {
        this.addCart(1, productId, 1, accountId);
      } catch (Exception e) {
        System.out.println("Please select a proper Product Id");
        continue;
      }

      System.out.println("Do you want to add more product y/n");
      String isAddProduct = in.nextLine();
      if ("n".contentEquals(isAddProduct)) {
        System.out.println("---------Moving to Cart---------------");
        isContinueToAddProduct = false;
        break;
      } else if ("y".contentEquals(isAddProduct)) {
        continue;
      }
    }
  }

  public void registerNewUser(Scanner in) {
    System.out.println("Enter username:");
    String emailAddress = in.nextLine();
    boolean ifUserExist = this.ifUserExists(emailAddress);
    if (ifUserExist) {
      System.out.println("Username already exists");
      this.registerNewUser(in);
    }
    System.out.println("Enter password:");
    String password1 = in.nextLine();
    System.out.println("Enter billing address:");
    String billingaddress = in.nextLine();
    System.out.println("Enter billing contact number:");
    int contact = Integer.parseInt(in.nextLine());
    System.out.println("Enter STATE:");
    String state = in.nextLine();
    this.userId = this.registerUser(emailAddress, password1, billingaddress, contact, state);
  }

  public List<String> getPaymentModes() {
    return ecommerceAppDao.getPaymentModes();
  }

  public void establishConnection() {
    try {
      ecommerceAppDao.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public int getUser(String username, String password) {
    return ecommerceAppDao.getUser(username, password);
  }

  public int registerUser(String emailAddress, String password, String address, int contact,
      String state) {
    return ecommerceAppDao.registerUser(emailAddress, password, address, contact, state);
  }

  public void addCart(int cartId, int productId, int quantity, int accountId) {
    ecommerceAppDao.addCart(cartId, productId, quantity, accountId);
  }

  public List<Category> getCategories() {

    return ecommerceAppDao.getCategories();
  }

  public List<Product> getProductsByCatgory(int categoryId) {

    return ecommerceAppDao.getProductsByCategory(categoryId);
  }

  public void insertOrderDetails(int orderid, int userid) {

    ecommerceAppDao.insertOrderDetails(orderid, userid);
  }

  public int insertOrders(String shippingAddress, int userid, String status) {

    return ecommerceAppDao.insertOrders(shippingAddress, userid, status);
  }

  public void insertPaymentInfo(int orderid, int paymentInfo, int accountId) {

    ecommerceAppDao.insertPaymentInfo(orderid, paymentInfo, accountId);
  }

  public List<Orders> getOrdersByUser(int userId) {
    return ecommerceAppDao.getOrdersByUser(userId);
  }

  public List<OrderDetail> getOrderDetails(int orderid) {
    return ecommerceAppDao.getOrderDetails(orderid);
  }

  public boolean ifUserExists(String email) {
    return ecommerceAppDao.ifUserExists(email);
  }

  public void cancelOrder(int orderid) {
    ecommerceAppDao.cancelOrder(orderid);
  }
}
