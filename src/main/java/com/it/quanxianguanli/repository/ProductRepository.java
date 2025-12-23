package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    List<Product> findByProductType(String productType);
    List<Product> findByIsStagnantAndIsReducedStock(Boolean isStagnant, Boolean isReducedStock);
}

