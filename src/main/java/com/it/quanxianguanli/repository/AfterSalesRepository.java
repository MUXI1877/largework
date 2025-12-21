package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AfterSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSalesRepository extends JpaRepository<AfterSales, String> {
    List<AfterSales> findByCustomerId(String customerId);
}

