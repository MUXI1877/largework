package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesInventoryReduction;
import com.it.quanxianguanli.entity.SalesInventoryReductionItem;
import com.it.quanxianguanli.repository.SalesInventoryReductionRepository;
import com.it.quanxianguanli.repository.SalesInventoryReductionItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesInventoryReductionService {

  @Autowired
  private SalesInventoryReductionRepository salesInventoryReductionRepository;

  @Autowired
  private SalesInventoryReductionItemRepository salesInventoryReductionItemRepository;

  public List<SalesInventoryReduction> findAll() {
    return salesInventoryReductionRepository.findAll();
  }

  public SalesInventoryReduction findById(String id) {
    return salesInventoryReductionRepository.findById(id).orElse(null);
  }

  public List<SalesInventoryReductionItem> findItemsByReductionId(String reductionId) {
    return salesInventoryReductionItemRepository.findByReductionId(reductionId);
  }

  @Transactional
  public SalesInventoryReduction save(SalesInventoryReduction reduction) {
    return salesInventoryReductionRepository.save(reduction);
  }

  @Transactional
  public SalesInventoryReductionItem saveItem(SalesInventoryReductionItem item) {
    return salesInventoryReductionItemRepository.save(item);
  }

  @Transactional
  public void deleteById(String id) {
    salesInventoryReductionItemRepository.findByReductionId(id).forEach(item -> salesInventoryReductionItemRepository.deleteById(item.getId()));
    salesInventoryReductionRepository.deleteById(id);
  }
}

