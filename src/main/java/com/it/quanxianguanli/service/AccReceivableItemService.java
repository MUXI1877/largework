package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.AccReceivableItem;
import com.it.quanxianguanli.repository.AccReceivableItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccReceivableItemService {

  @Autowired
  private AccReceivableItemRepository accReceivableItemRepository;

  public List<AccReceivableItem> findAll() {
    return accReceivableItemRepository.findAll();
  }

  public List<AccReceivableItem> findByContractId(String contractId) {
    return accReceivableItemRepository.findByContractId(contractId);
  }

  public AccReceivableItem findById(String id) {
    return accReceivableItemRepository.findById(id).orElse(null);
  }

  @Transactional
  public AccReceivableItem save(AccReceivableItem accReceivableItem) {
    return accReceivableItemRepository.save(accReceivableItem);
  }

  @Transactional
  public void deleteById(String id) {
    accReceivableItemRepository.deleteById(id);
  }
}

