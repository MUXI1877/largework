package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.QuotationItem;
import com.it.quanxianguanli.repository.QuotationItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuotationItemService {

  @Autowired
  private QuotationItemRepository quotationItemRepository;

  public List<QuotationItem> findAll() {
    return quotationItemRepository.findAll();
  }

  public List<QuotationItem> findByQuotationId(String quotationId) {
    return quotationItemRepository.findByQuotationId(quotationId);
  }

  public QuotationItem findById(String id) {
    return quotationItemRepository.findById(id).orElse(null);
  }

  @Transactional
  public QuotationItem save(QuotationItem quotationItem) {
    return quotationItemRepository.save(quotationItem);
  }

  @Transactional
  public void deleteById(String id) {
    quotationItemRepository.deleteById(id);
  }
}

