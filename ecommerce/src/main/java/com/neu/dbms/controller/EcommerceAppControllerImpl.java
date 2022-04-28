package com.neu.dbms.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.neu.dbms.service.EcommerceAppServiceImpl;

@RestController
@RequestMapping("ecommerce")
public class EcommerceAppControllerImpl implements EcommerceAppController {

  @Autowired
  private EcommerceAppServiceImpl ecommerceAppService;

  @GetMapping("getUsers")
  public String getAllUsers(@RequestParam("username") String username,
      @RequestParam("password") String password) {
    System.out.println("Executed Get user");
    return ecommerceAppService.getAllProducts(username, password);
  }

  @PostMapping("addUser/{name}")
  public void addUser(@PathParam("name") String name) {
    System.out.println("Executed Add user");
    // ecommerceAppService.addUser();
  }

  @GetMapping("getCategories")
  public void getCategories() {
    System.out.println("Executed get categories");
    // ecommerceAppService.getAllProducts();
  }

  @PostMapping("test")
  public void getProductsByCategoryId() {
    System.out.println("Executed get prodcts by category Id");
    // ecommerceAppService.getAllProducts();
  }

  @GetMapping("test")
  public void getAddToCart() {
    System.out.println("Executed Add to cart");
    // ecommerceAppService.getAllProducts();
  }
}
