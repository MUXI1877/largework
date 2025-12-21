package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.CustomerVisit;
import com.it.quanxianguanli.repository.CustomerVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerVisitService {

  @Autowired
  private CustomerVisitRepository customerVisitRepository;

  public List<CustomerVisit> findAll() {
    return customerVisitRepository.findAll();
  }

  public CustomerVisit findById(String id) {
    return customerVisitRepository.findById(id).orElse(null);
  }

  @Transactional
  public CustomerVisit save(CustomerVisit customerVisit) {
    return customerVisitRepository.save(customerVisit);
  }

  @Transactional
  public void deleteById(String id) {
    customerVisitRepository.deleteById(id);
  }
}

