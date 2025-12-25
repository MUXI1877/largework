package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesInventoryReduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesInventoryReductionRepository extends JpaRepository<SalesInventoryReduction, String> {
}

