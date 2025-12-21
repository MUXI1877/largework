package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Customer;
import com.it.quanxianguanli.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Customer findById(String id) {
    return customerRepository.findById(id).orElse(null);
  }

  @Transactional
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  @Transactional
  public void deleteById(String id) {
    customerRepository.deleteById(id);
  }
}
