package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.entity.SalesOrderItem;
import com.it.quanxianguanli.repository.SalesOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalesOrderItemService {

    @Autowired
    private SalesOrderItemRepository salesOrderItemRepository;

    @Transactional(readOnly = true)
    public List<SalesOrderItem> findAll() {
        return salesOrderItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SalesOrderItem> findById(String id) {
        return salesOrderItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SalesOrderItem> findByOrder(SalesOrder order) {
        return salesOrderItemRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public List<SalesOrderItem> findByProductId(String productId) {
        return salesOrderItemRepository.findByProductId(productId);
    }

    @Transactional
    public SalesOrderItem save(SalesOrderItem salesOrderItem) {
        return salesOrderItemRepository.save(salesOrderItem);
    }

    @Transactional
    public void deleteById(String id) {
        salesOrderItemRepository.deleteById(id);
    }

    @Transactional
    public List<SalesOrderItem> saveAll(List<SalesOrderItem> items) {
        return salesOrderItemRepository.saveAll(items);
    }

    @Transactional
    public void deleteByOrder(SalesOrder order) {
        List<SalesOrderItem> items = salesOrderItemRepository.findByOrder(order);
        salesOrderItemRepository.deleteAll(items);
    }
}