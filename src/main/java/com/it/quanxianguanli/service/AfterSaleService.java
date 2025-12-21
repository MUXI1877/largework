package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AfterSale;
import com.it.quanxianguanli.repository.AfterSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AfterSaleService {

  @Autowired
  private AfterSaleRepository afterSaleRepository;

  public List<AfterSale> findAll() {
    return afterSaleRepository.findAll();
  }

  public List<AfterSale> findByDeviceSn(String deviceSn) {
    return afterSaleRepository.findByDeviceSn(deviceSn);
  }

  public List<AfterSale> findByStatus(String status) {
    return afterSaleRepository.findByStatus(status);
  }

  public List<AfterSale> findByHandlerId(String handlerId) {
    return afterSaleRepository.findByHandlerId(handlerId);
  }

  public AfterSale findById(String id) {
    return afterSaleRepository.findById(id).orElse(null);
  }

  @Transactional
  public AfterSale save(AfterSale afterSale) {
    return afterSaleRepository.save(afterSale);
  }

  @Transactional
  public void deleteById(String id) {
    afterSaleRepository.deleteById(id);
  }
}

