package com.neu.dbms.controller;

import java.util.List;

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
import com.neu.dbms.service.EcommerceAppServiceImpl;

@RestController
@RequestMapping("ecommerce")
public class EcommerceAppControllerImpl implements EcommerceAppController {

  @Autowired
  private EcommerceAppDaoImpl ecommerceAppDao;

  @GetMapping("getUsers")
  public String getAllUsers(@RequestParam("username") String username,
      @RequestParam("password") String password) {
    System.out.println("Executed Get user");
    return ecommerceAppDao.getUser(username, password);
  }

  @PostMapping("addUser/{name}")
  public void addUser(@PathParam("name") String name) {
    System.out.println("Executed Add user");
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

  @PostMapping("getProductsByCategory")
  public void getProductsByCatgory(@RequestParam("catId") int categoryId) {
    System.out.println("Executed get prodcts by category Id");
    ecommerceAppDao.getProductsByCatgory(categoryId);
  }

  @GetMapping("test")
  public void getAddToCart() {
    System.out.println("Executed Add to cart");
    // ecommerceAppService.getAllProducts();
  }
}
