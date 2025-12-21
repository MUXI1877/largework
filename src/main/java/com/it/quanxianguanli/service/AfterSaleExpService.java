package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AfterSaleExp;
import com.it.quanxianguanli.repository.AfterSaleExpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AfterSaleExpService {

  @Autowired
  private AfterSaleExpRepository afterSaleExpRepository;

  public List<AfterSaleExp> findAll() {
    return afterSaleExpRepository.findAll();
  }

  public List<AfterSaleExp> findByDeviceModel(String deviceModel) {
    return afterSaleExpRepository.findByDeviceModel(deviceModel);
  }

  public List<AfterSaleExp> findByProblemContaining(String problem) {
    return afterSaleExpRepository.findByProblemContaining(problem);
  }

  public AfterSaleExp findById(String id) {
    return afterSaleExpRepository.findById(id).orElse(null);
  }

  @Transactional
  public AfterSaleExp save(AfterSaleExp afterSaleExp) {
    return afterSaleExpRepository.save(afterSaleExp);
  }

  @Transactional
  public void deleteById(String id) {
    afterSaleExpRepository.deleteById(id);
  }
}

