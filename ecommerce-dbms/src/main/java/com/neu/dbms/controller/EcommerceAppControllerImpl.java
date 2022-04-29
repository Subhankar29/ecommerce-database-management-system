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
import com.neu.dbms.model.Category;
import com.neu.dbms.model.Product;
import com.neu.dbms.service.EcommerceAppServiceImpl;

@RestController
@RequestMapping("ecommerce")
public class EcommerceAppControllerImpl implements EcommerceAppController {

  @Autowired
  private EcommerceAppDaoImpl ecommerceAppDao;

  @GetMapping("/")
  public void getConnection() {
    System.out.println("Executed Get connection");
    System.out.println("Enter username");
    Scanner in = new Scanner(System.in);
    String username = in.nextLine();
    String password = in.nextLine();
    String response = this.getAllUsers(username, password);
    System.out.println(response);
    if(response.contains("Invalid")) {
      System.out.println("Do you want to register user ? (y/n)");
      String ifregister = in.nextLine();
      if("y".contentEquals(ifregister)) {
        String emailAddress = in.nextLine();
        String password1 = in.nextLine();
        String billingaddress = in.nextLine();
        int contact = Integer.parseInt(in.nextLine());
        String state = in.nextLine();
        this.registerUser(emailAddress, password1, billingaddress, contact, state);
      }else {
        System.out.print("Operation stopped");
        in.close();
      }
    }else {
      List<Category> categoryList = this.getCategories();
      System.out.println("\n");
      categoryList.forEach(s->{
        System.out.println(s.getCategoryId() + "--->" + s.getCategoryName());
      });
      System.out.println("\nChoose a category (Enter the id)");
      int selectedCategory = Integer.parseInt(in.nextLine());
      List<Product> productList = this.getProductsByCatgory(selectedCategory);
      
    }
    //return ecommerceAppDao.getUser(username, password);
  }
  
  @GetMapping("getUsers")
  public String getAllUsers(@RequestParam("username") String username,
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

  @GetMapping("getCategories")
  public List<Category> getCategories() {
    System.out.println("Executed get categories");
    return ecommerceAppDao.getCategories();
  }

//  @PostMapping("getProductsByCategory")
//  public void getProductsByCatgory(@RequestParam("catId") int categoryId) {
//    System.out.println("Executed get prodcts by category Id");
//    ecommerceAppDao.getProductsByCategory(categoryId);
//  }
  
  @GetMapping("getProductsByCategory")
  public void getProductsByCatgory(@RequestParam("catId") int categoryId) {
    System.out.println("Executed get prodcts by category Id");
    ecommerceAppDao.getProductsByCategory(categoryId);
  }

  @GetMapping("test")
  public void getAddToCart() {
    System.out.println("Executed Add to cart");
    // ecommerceAppService.getAllProducts();
  }
}
