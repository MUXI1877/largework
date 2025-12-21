package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AccReceivablePlan;
import com.it.quanxianguanli.repository.AccReceivablePlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccReceivablePlanService {

  @Autowired
  private AccReceivablePlanRepository accReceivablePlanRepository;

  public List<AccReceivablePlan> findAll() {
    return accReceivablePlanRepository.findAll();
  }

  public List<AccReceivablePlan> findByContractId(String contractId) {
    return accReceivablePlanRepository.findByContractId(contractId);
  }

  public AccReceivablePlan findById(String id) {
    return accReceivablePlanRepository.findById(id).orElse(null);
  }

  @Transactional
  public AccReceivablePlan save(AccReceivablePlan accReceivablePlan) {
    return accReceivablePlanRepository.save(accReceivablePlan);
  }

  @Transactional
  public void deleteById(String id) {
    accReceivablePlanRepository.deleteById(id);
  }
}

