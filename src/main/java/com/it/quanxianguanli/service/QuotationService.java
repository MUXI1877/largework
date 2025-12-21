package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Quotation;
import com.it.quanxianguanli.repository.QuotationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuotationService {

  @Autowired
  private QuotationRepository quotationRepository;

  public List<Quotation> findAll() {
    return quotationRepository.findAll();
  }

  public Quotation findById(String id) {
    return quotationRepository.findById(id).orElse(null);
  }

  @Transactional
  public Quotation save(Quotation quotation) {
    return quotationRepository.save(quotation);
  }

  @Transactional
  public void deleteById(String id) {
    quotationRepository.deleteById(id);
  }
}

