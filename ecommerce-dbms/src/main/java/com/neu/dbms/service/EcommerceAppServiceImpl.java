package com.neu.dbms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.neu.dbms.dao.EcommerceAppDao;

@Service
public class EcommerceAppServiceImpl implements EcommerceAppService {

  @Autowired
  private EcommerceAppDao ecommerceAppDao;

  @Override
  public String getAllProducts(String user_name, String pass_word) {
    return ecommerceAppDao.getAllProducts( user_name,  pass_word);
  }
}
