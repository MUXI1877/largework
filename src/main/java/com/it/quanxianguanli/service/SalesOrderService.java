package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Transactional(readOnly = true)
    public List<SalesOrder> findAll() {
        return salesOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SalesOrder> findById(String id) {
        return salesOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        return salesOrderRepository.findByOrderNo(orderNo);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findByStatus(String status) {
        return salesOrderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findByCustomerId(String customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<SalesOrder> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return salesOrderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Transactional
    public SalesOrder save(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }

    @Transactional
    public void deleteById(String id) {
        salesOrderRepository.deleteById(id);
    }

    @Transactional
    public SalesOrder update(SalesOrder salesOrder) {
        Optional<SalesOrder> existing = salesOrderRepository.findById(salesOrder.getId());
        if (existing.isPresent()) {
            return salesOrderRepository.save(salesOrder);
        } else {
            throw new RuntimeException("销售订单不存在");
        }
    }

    @Transactional
    public SalesOrder updateStatus(String id, String status) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("销售订单不存在"));
        salesOrder.setStatus(status);
        return salesOrderRepository.save(salesOrder);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesAmount(LocalDate startDate, LocalDate endDate) {
        List<SalesOrder> orders = salesOrderRepository.findByOrderDateBetween(startDate, endDate);
        return orders.stream()
                .map(SalesOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public long getOrderCountByStatus(String status) {
        List<SalesOrder> orders = salesOrderRepository.findByStatus(status);
        return orders.size();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getOrderCountByStatus() {
        List<SalesOrder> orders = salesOrderRepository.findAll();
        Map<String, Long> result = new HashMap<>();
        orders.forEach(order -> {
            result.put(order.getStatus(), result.getOrDefault(order.getStatus(), 0L) + 1);
        });
        return result;
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, BigDecimal> getDailySalesReport(LocalDate startDate, LocalDate endDate) {
        List<SalesOrder> orders = salesOrderRepository.findByOrderDateBetween(startDate, endDate);
        Map<LocalDate, BigDecimal> result = new HashMap<>();
        orders.forEach(order -> {
            LocalDate orderDate = order.getOrderDate();
            result.put(orderDate, result.getOrDefault(orderDate, BigDecimal.ZERO).add(order.getTotalAmount()));
        });
        return result;
    }

    @Transactional(readOnly = true)
    public long getTotalOrderCount() {
        return salesOrderRepository.count();
    }
}