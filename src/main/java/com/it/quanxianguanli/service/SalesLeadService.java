package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesLead;
import com.it.quanxianguanli.repository.SalesLeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesLeadService {

  @Autowired
  private SalesLeadRepository salesLeadRepository;

  public List<SalesLead> findAll() {
    return salesLeadRepository.findAll();
  }

  public SalesLead findById(String id) {
    return salesLeadRepository.findById(id).orElse(null);
  }

  @Transactional
  public SalesLead save(SalesLead salesLead) {
    return salesLeadRepository.save(salesLead);
  }

  @Transactional
  public void deleteById(String id) {
    salesLeadRepository.deleteById(id);
  }
}

