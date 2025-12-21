package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.repository.PriceBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PriceBookService {

  @Autowired
  private PriceBookRepository priceBookRepository;

  public List<PriceBook> findAll() {
    return priceBookRepository.findAll();
  }

  public PriceBook findById(String id) {
    return priceBookRepository.findById(id).orElse(null);
  }

  @Transactional
  public PriceBook save(PriceBook priceBook) {
    return priceBookRepository.save(priceBook);
  }

  @Transactional
  public void deleteById(String id) {
    priceBookRepository.deleteById(id);
  }
}

