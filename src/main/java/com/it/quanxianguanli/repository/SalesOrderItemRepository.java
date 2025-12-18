package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, String> {
    List<SalesOrderItem> findByOrder(SalesOrder order);
    List<SalesOrderItem> findByProductId(String productId);
}