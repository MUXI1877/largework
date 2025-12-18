package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, String> {
    Optional<SalesOrder> findByOrderNo(String orderNo);
    List<SalesOrder> findBySalesUser(SysUser salesUser);
    List<SalesOrder> findByStatus(String status);
    List<SalesOrder> findByCustomerId(String customerId);
    List<SalesOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
}