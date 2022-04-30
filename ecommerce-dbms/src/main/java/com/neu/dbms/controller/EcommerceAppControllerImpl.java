package com.neu.dbms.controller;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.dbms.dao.EcommerceAppDaoImpl;
import com.neu.dbms.model.CartProduct;
import com.neu.dbms.model.Category;
import com.neu.dbms.model.OrderDetail;
import com.neu.dbms.model.Orders;
import com.neu.dbms.model.Product;

@RestController
@RequestMapping("ecommerce")
public class EcommerceAppControllerImpl implements EcommerceAppController {

  @Autowired
  private EcommerceAppDaoImpl ecommerceAppDao;
  double totalVal = 0;
  int paymentIndex = 0;
  int userId = 0;
  int accountId = 0;

  @GetMapping("/")
  public void startApplication() {
    this.establishConnection();
    
    Scanner in = new Scanner(System.in);
    
    String username = "";
    
    while (username.isBlank()) {
      System.out.println("Enter username");
      username = in.nextLine();
    }
    
    String password = "";
    
    while(password.isBlank()) {
      System.out.println("Enter Password");
      password = in.nextLine();
    }
    
    this.userId = this.getUser(username, password);
    
//    System.out.println(this.userId);
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
    
    while(!isMainValidInput) {
      System.out.println("----------------SELECT AN ACTION--------------");
      System.out.println("SELECT 1. TO SEARCH FOR A PRODUCT");
      System.out.println("SELECT 2. TO MANAGE ORDERS");
      System.out.println("SELECT 3. TO LOG OUT");

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
      
      if (action == 1) {

        accountId = ecommerceAppDao.getAccountId(username);
        System.out.println("Account Id --->" + accountId);
        
        addProducts(in);

        addToCart(in);

        moveToShipping(in);
      } else if (action == 2) {
          manageOrders(in);
      } else if (action == 3) {
        System.out.println("LOGGIN OUT");
        in.close();
        isValidAction = true;
        break;
      }
    }
  }
  
  public void manageOrders(Scanner in) {
    List<Orders> orderList = this.getOrdersByUser(this.userId);
    
    System.out.println(String.format("%-15s%-15s%-15s%-15s", "orderId", "date Created", "status", "total"));
    
    orderList.forEach(s->{
      System.out.println(String.format("%-15s%-15s%-15s%-15s",s.getOrderId(), s.getDateCreated(), s.getStatus(), s.getTotal()));
    });
    
    System.out.println("Enter the orderId:");
    int orderid = Integer.parseInt(in.nextLine());
    
    List<OrderDetail> orderDetailList = this.getOrderDetails(orderid);
    
    System.out.println(String.format("%-15s%-15s%-15s%-15s%-15s%-25s", "ProductName", "UnitCost", "SubTotal", "Status", "PaymentMode", "Address"));
    
    orderDetailList.forEach(t->{
      System.out.println(String.format("%-15s%-15s%-15s%-15s%-15s%-25s", t.getProductName(),  t.getUnitCost(), t.getSubTotal(), t.getStatus(), t.getPaymentMode(), t.getShippingAddress()));
    });
    
    System.out.println("Please select if you want to cancel the order(y/n) ?");
    String ifCancel = in.nextLine();
    if("y".equalsIgnoreCase(ifCancel)) {
      this.cancelOrder(orderid);
      System.out.println("Order canceled");
    }
  }
  
  public void moveToShipping(Scanner in) {
    System.out.println("----------PROCEED TO SHIPPING--------");
    System.out.println("Enter Shipping Address:");
    String shipAdd = in.nextLine();
    System.out.println("Enter userid:");
    int orderid = this.insertOrders(shipAdd, this.userId, "Placed");
    this.insertOrderDetails(orderid);
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
      
      System.out.println(String.format("%-15s%-15s%-15s%-15s", "Product Id", "Name", "Quantity", "Sub Total"));
      
      productList.forEach(product -> {
        System.out.println(String.format("%-15s%-15s%-15s%-15s", product.getProductId(), product.getName(), product.getQuantity(), product.getSubtotal()));
        
        totalVal = totalVal + product.getSubtotal();
      });

      System.out.println("Total cart amount: " + totalVal);

      System.out.println(
          "Do you want to update cart or go to checkout? Press Y to update cart or N to checkout");

      String isUpdateCart = in.nextLine();
      if ("y".contentEquals(isUpdateCart)) {
        
        boolean isValidProduct = false;
        
        while (!isValidProduct) {
          try {
            System.out.println("Select product id to update the quantity");
            int productId = in.nextInt();
            System.out.println("Please sepcify the product quantity");
            int quantity = in.nextInt();
            ecommerceAppDao.updateCart(accountId, productId, quantity);
            isValidProduct = true;
            break;
          } catch (Exception e) {
            System.out.println("Execution failed please try again");
            continue;
          } 
        }
        
      } else {
        isAddToCart = false;
        break;
      }
    }
  }
  
  public void addProducts(Scanner in) {
    boolean isContinueToAddProduct = true;

    while (isContinueToAddProduct) {
      List<Category> categoryList = this.getCategories();
      System.out.println("\n");

      System.out.println(String.format("%-15s%-15s", "Categoty Id", "Categoty Name"));

      categoryList.forEach(s -> {
        System.out.println(String.format("%-15s%-15s", s.getCategoryId(), s.getCategoryName()));
      });
      System.out.println("\nChoose a category (Enter the id)");
      int selectedCategory = Integer.parseInt(in.nextLine());
      List<Product> productList = this.getProductsByCatgory(selectedCategory);
      
      System.out.println(String.format("%-30s%-30s%-30s%-30s", "Product Id", "Name", "Price", "Description"));

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
    }catch(SQLException e) {
      e.printStackTrace();
    }
     
  }


  @GetMapping("getUsers")
  public int getUser(@RequestParam("username") String username,
      @RequestParam("password") String password) {
    System.out.println("Executed Get user");
    return ecommerceAppDao.getUser(username, password);
  }

  @PostMapping("registerUser")
  public int registerUser(@RequestParam("emailAddress") String emailAddress,
      @RequestParam("password") String password, @RequestParam("address") String address,
      @RequestParam("contact") int contact, @RequestParam("state") String state) {
    System.out.println("Executed Add user");
    return ecommerceAppDao.registerUser(emailAddress, password, address, contact, state);
  }

  @PostMapping("addCart")
  public void addCart(@RequestParam("cartId") int cartId, @RequestParam("productId") int productId,
      @RequestParam("quantity") int quantity, @RequestParam("accountId") int accountId) {
    System.out.println("Executed Add cart");
    ecommerceAppDao.addCart(cartId, productId, quantity, accountId);
  }

//  @PostMapping("getProductsByCategory")
//  public void getProductsByCatgory(@RequestParam("catId") int categoryId) {
//    System.out.println("Executed get prodcts by category Id");
//    ecommerceAppDao.getProductsByCategory(categoryId);
//  }

  @GetMapping("test")
  public void getAddToCart() {
    System.out.println("Executed Add to cart");
    // ecommerceAppService.getAllProducts();
  }

  @GetMapping("getCategories")
  public List<Category> getCategories() {
    System.out.println("Executed get categories");
    return ecommerceAppDao.getCategories();
  }

  @GetMapping("getProductsByCategory")
  public List<Product> getProductsByCatgory(@RequestParam("catId") int categoryId) {
    System.out.println("Executed get prodcts by category Id");
    return ecommerceAppDao.getProductsByCategory(categoryId);
  }

  @PostMapping("insertOrderDetails")
  public void insertOrderDetails(@RequestParam("orderid") int orderid) {
    System.out.println("Executed insert order details");
    ecommerceAppDao.insertOrderDetails(orderid);
  }

  @PostMapping("insertOrders")
  public int insertOrders(@RequestParam("shipAdd") String shippingAddress,
      @RequestParam("userid") int userid, @RequestParam("status") String status) {
    System.out.println("Executed insert orders");
    return ecommerceAppDao.insertOrders(shippingAddress, userid, status);
  }

  @PostMapping("insertPaymentInfo")
  public void insertPaymentInfo(@RequestParam("orderid") int orderid,
      @RequestParam("paymentInfo") int paymentInfo, @RequestParam("accountId") int accountId) {
    System.out.println("Executed insert payment info");
    ecommerceAppDao.insertPaymentInfo(orderid, paymentInfo, accountId);
  }
  
  public List<Orders> getOrdersByUser(int userId) {
   return ecommerceAppDao.getOrdersByUser(userId);
  }

  public List<OrderDetail> getOrderDetails(int orderid) {
   return ecommerceAppDao.getOrderDetails(orderid);
  }

  public void cancelOrder(int orderid) {
    ecommerceAppDao.cancelOrder(orderid);
  }
}
