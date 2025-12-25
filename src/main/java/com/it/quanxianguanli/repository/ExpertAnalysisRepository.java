package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ExpertAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertAnalysisRepository extends JpaRepository<ExpertAnalysis, String> {
  List<ExpertAnalysis> findByDeviceIdOrderByAnalysisTimeDesc(String deviceId);
}

