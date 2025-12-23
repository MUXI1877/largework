package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ReceivablePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReceivablePlanRepository extends JpaRepository<ReceivablePlan, String>, JpaSpecificationExecutor<ReceivablePlan> {

    List<ReceivablePlan> findByContractCodeOrderByDueDateAscCreateTimeAsc(String contractCode);
}

