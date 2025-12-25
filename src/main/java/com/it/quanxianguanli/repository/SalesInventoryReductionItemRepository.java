package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesInventoryReductionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesInventoryReductionItemRepository extends JpaRepository<SalesInventoryReductionItem, String> {
  List<SalesInventoryReductionItem> findByReductionId(String reductionId);
}

