package com.neu.dbms.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import com.neu.dbms.controller.EcommerceAppControllerImpl;

@SpringBootApplication
@ComponentScan(basePackages = { "com.neu.dbms" })
public class EcommerceApp {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceApp.class, args);
  }

}
