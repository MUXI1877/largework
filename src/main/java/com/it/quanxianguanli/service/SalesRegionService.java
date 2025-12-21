package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesRegion;
import com.it.quanxianguanli.repository.SalesRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesRegionService {

  @Autowired
  private SalesRegionRepository salesRegionRepository;

  public List<SalesRegion> findAll() {
    return salesRegionRepository.findAll();
  }

  public SalesRegion findById(String id) {
    return salesRegionRepository.findById(id).orElse(null);
  }

  @Transactional
  public SalesRegion save(SalesRegion salesRegion) {
    return salesRegionRepository.save(salesRegion);
  }

  @Transactional
  public void deleteById(String id) {
    salesRegionRepository.deleteById(id);
  }
}
