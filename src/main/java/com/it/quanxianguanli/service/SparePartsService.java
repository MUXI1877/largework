package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SpareParts;
import com.it.quanxianguanli.repository.SparePartsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SparePartsService {

  @Autowired
  private SparePartsRepository sparePartsRepository;

  public List<SpareParts> findAll() {
    return sparePartsRepository.findAll();
  }

  public SpareParts findById(String id) {
    return sparePartsRepository.findById(id).orElse(null);
  }

  @Transactional
  public SpareParts save(SpareParts spareParts) {
    return sparePartsRepository.save(spareParts);
  }

  @Transactional
  public void deleteById(String id) {
    sparePartsRepository.deleteById(id);
  }
}

