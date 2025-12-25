package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ExpertAnalysis;
import com.it.quanxianguanli.repository.ExpertAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpertAnalysisService {

  @Autowired
  private ExpertAnalysisRepository expertAnalysisRepository;

  public List<ExpertAnalysis> findAll() {
    return expertAnalysisRepository.findAll();
  }

  public ExpertAnalysis findById(String id) {
    return expertAnalysisRepository.findById(id).orElse(null);
  }

  public List<ExpertAnalysis> findByDeviceId(String deviceId) {
    return expertAnalysisRepository.findByDeviceIdOrderByAnalysisTimeDesc(deviceId);
  }

  @Transactional
  public ExpertAnalysis save(ExpertAnalysis analysis) {
    return expertAnalysisRepository.save(analysis);
  }

  @Transactional
  public void deleteById(String id) {
    expertAnalysisRepository.deleteById(id);
  }
}

