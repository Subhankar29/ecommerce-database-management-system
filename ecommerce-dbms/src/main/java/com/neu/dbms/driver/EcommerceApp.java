package com.neu.dbms.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.neu.dbms" })
public class EcommerceApp {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceApp.class, args);
  }

}
