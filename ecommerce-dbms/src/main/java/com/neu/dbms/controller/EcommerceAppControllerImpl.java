package com.neu.dbms.controller;

import java.util.List;
import java.util.Scanner;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.dbms.dao.EcommerceAppDaoImpl;
import com.neu.dbms.model.CartProduct;
import com.neu.dbms.model.Category;
import com.neu.dbms.model.Product;
import com.neu.dbms.service.EcommerceAppServiceImpl;

@RestController
@RequestMapping("ecommerce")
public class EcommerceAppControllerImpl implements EcommerceAppController {

  @Autowired
  private EcommerceAppDaoImpl ecommerceAppDao;
  double totalVal = 0;
  int paymentIndex =0;
  
  @GetMapping("/")
  public void getConnection() {
    System.out.println("Executed Get connection");
    System.out.println("Enter username");
    Scanner in = new Scanner(System.in);
    String username = in.nextLine();
    System.out.println("Enter Password");
    String password = in.nextLine();
    String response = this.getUser(username, password);
   
   
    System.out.println(response);
    if (response.contains("Invalid")) {
      System.out.println("Do you want to register user ? (y/n)");
      String ifregister = in.nextLine();
      if ("y".contentEquals(ifregister)) {
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
        this.registerUser(emailAddress, password1, billingaddress, contact, state);
      } else {
        System.out.print("Operation stopped");
        in.close();
      }
    } 
    
    int accountId = ecommerceAppDao.getAccountId(username);
    System.out.println("User Id --->" + accountId);
    
    boolean isContinueToAddProduct = true;
    
    while (isContinueToAddProduct) {
      List<Category> categoryList = this.getCategories();
      System.out.println("\n");
      
      System.out.println("Categoty Id\tCategoty Name");
      
      categoryList.forEach(s->{
        System.out.println(String.format("%3s%20s", s.getCategoryId(), s.getCategoryName()));
      });
      System.out.println("\nChoose a category (Enter the id)");
      int selectedCategory = Integer.parseInt(in.nextLine());
      List<Product> productList = this.getProductsByCatgory(selectedCategory);
      
      System.out.println("Product Id\t\tProduct Name\t\tDescription\t\t\tPrice");
      
      productList.forEach(product->{
        System.out.println(String.format("%3s%30s%25s%30s", product.getProductId(),  product.getName(),  product.getDescription(), product.getPrice()));
      });
      
      System.out.println("Select a product (product Id) to add to cart");
      int productId =  Integer.parseInt(in.nextLine());
      this.addCart(1, productId, 1, accountId);
      
      System.out.println("Do you want to add more product y/n");
      String isAddProduct = in.nextLine();
      if ("n".contentEquals(isAddProduct)) {
        System.out.println("Moving to Cart");
        isContinueToAddProduct = false;
        break;
      }
    }
    
    boolean isAddToCart = true;
    
    while (isAddToCart) {
      System.out.println("Products in cart:");
      List<CartProduct> productList = ecommerceAppDao.getProductsFromCart(accountId);
     
      totalVal = 0;
      productList.forEach(product->{
        System.out.println(product.getProductId() + "--->" + product.getName() + " quantity --->" + product.getQuantity() + " sub-total --->" + product.getSubtotal() + " ");
        totalVal = totalVal + product.getSubtotal();
      });
      
     System.out.println("Total cart amount: " + totalVal);
     
     System.out.println("Do you want to update cart or go to checkout? Press Y to update cart or N to checkout");
     
     String isUpdateCart = in.nextLine();
     if ("y".contentEquals(isUpdateCart)) {
       System.out.println("Select product id to update the quantity");
       int productId = in.nextInt();
       System.out.println("Please sepcify the product quantity");
       int quantity = in.nextInt();
       ecommerceAppDao.updateCart(accountId, productId, quantity);
     } else {
       isAddToCart = false; 
       break;
     } 
    }
    
    System.out.println("----------PROCEED TO SHIPPING--------");
    //return ecommerceAppDao.getUser(username, password);
  System.out.println("Checkout");
  System.out.println("Click c to check out:");
  String checkout = in.nextLine();
  if ("C".equalsIgnoreCase(checkout)) {
  System.out.println("Enter Shipping Address:");
  String shipAdd = in.nextLine();
  System.out.println("Enter userid:");
  int userid = 143;
  int orderid = this.insertOrders(shipAdd, userid, "Placed");
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
  }
  
  public List<String> getPaymentModes() {
    return ecommerceAppDao.getPaymentModes();
    }
  
  
  
  @GetMapping("getUsers")
  public String getUser(@RequestParam("username") String username,
      @RequestParam("password") String password) {
    System.out.println("Executed Get user");
    return ecommerceAppDao.getUser(username, password);
  }

  @PostMapping("registerUser")
  public void registerUser(
      @RequestParam("emailAddress") String emailAddress, 
      @RequestParam("password") String password, 
      @RequestParam("address") String address,
      @RequestParam("contact") int contact,
      @RequestParam("state") String state) {
    System.out.println("Executed Add user");
    ecommerceAppDao.registerUser(emailAddress, password, address, contact, state);
  }
  
  @PostMapping("addCart")
  public void addCart(@RequestParam("cartId") int cartId, @RequestParam("productId") int productId, @RequestParam("quantity") int quantity, @RequestParam("accountId") int accountId) {
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
}
