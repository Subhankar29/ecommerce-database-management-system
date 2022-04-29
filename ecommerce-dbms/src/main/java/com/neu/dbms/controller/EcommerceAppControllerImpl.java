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
import com.neu.dbms.model.Product;
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
    // ecommerceAppService.addUser();
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
  public void insertOrders(@RequestParam("shipAdd") String shippingAddress,
      @RequestParam("userid") int userid, @RequestParam("status") String status) {
    System.out.println("Executed insert orders");
    ecommerceAppDao.insertOrders(shippingAddress, userid, status);
  }

  @PostMapping("insertPaymentInfo")
  public void insertPaymentInfo(@RequestParam("orderid") int orderid,
      @RequestParam("paymentInfo") String paymentInfo) {
    System.out.println("Executed insert payment info");
    ecommerceAppDao.insertPaymentInfo(orderid, paymentInfo);
  }
}
