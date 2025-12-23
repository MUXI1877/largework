package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.PriceBookLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceBookLogRepository extends JpaRepository<PriceBookLog, String>, JpaSpecificationExecutor<PriceBookLog> {
    
    /**
     * 根据价格本ID查找所有日志
     */
    List<PriceBookLog> findByPriceBookIdOrderByCreateTimeDesc(String priceBookId);
    
    /**
     * 根据产品ID查找所有日志
     */
    List<PriceBookLog> findByProductIdOrderByCreateTimeDesc(String productId);
    
    /**
     * 根据操作人ID查找所有日志
     */
    List<PriceBookLog> findByOperatorIdOrderByCreateTimeDesc(String operatorId);
}

