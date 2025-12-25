package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AfterSaleStore;
import com.it.quanxianguanli.repository.AfterSaleStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AfterSaleStoreService {

  @Autowired
  private AfterSaleStoreRepository afterSaleStoreRepository;

  public List<AfterSaleStore> findAll() {
    return afterSaleStoreRepository.findAll();
  }

  public AfterSaleStore findById(String id) {
    return afterSaleStoreRepository.findById(id).orElse(null);
  }

  @Transactional
  public AfterSaleStore save(AfterSaleStore store) {
    return afterSaleStoreRepository.save(store);
  }

  @Transactional
  public void deleteById(String id) {
    afterSaleStoreRepository.deleteById(id);
  }
}

