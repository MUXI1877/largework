package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AfterSalePlan;
import com.it.quanxianguanli.repository.AfterSalePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AfterSalePlanService {

  @Autowired
  private AfterSalePlanRepository afterSalePlanRepository;

  public List<AfterSalePlan> findAll() {
    return afterSalePlanRepository.findAll();
  }

  public AfterSalePlan findById(String id) {
    return afterSalePlanRepository.findById(id).orElse(null);
  }

  @Transactional
  public AfterSalePlan save(AfterSalePlan plan) {
    return afterSalePlanRepository.save(plan);
  }

  @Transactional
  public void deleteById(String id) {
    afterSalePlanRepository.deleteById(id);
  }
}

