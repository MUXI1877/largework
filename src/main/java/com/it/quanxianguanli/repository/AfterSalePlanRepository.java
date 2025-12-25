package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AfterSalePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterSalePlanRepository extends JpaRepository<AfterSalePlan, String> {
}

