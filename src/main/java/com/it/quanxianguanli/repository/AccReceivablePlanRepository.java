package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AccReceivablePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccReceivablePlanRepository extends JpaRepository<AccReceivablePlan, String> {
  List<AccReceivablePlan> findByContractId(String contractId);
}

