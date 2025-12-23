package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.PriceBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceBookRepository extends JpaRepository<PriceBook, String>, JpaSpecificationExecutor<PriceBook> {
    
    /**
     * 根据产品ID和部门查找价格本
     */
    Optional<PriceBook> findByProductIdAndDepartment(String productId, String department);
    
    /**
     * 根据产品ID查找所有部门的价格本
     */
    List<PriceBook> findByProductId(String productId);
    
    /**
     * 根据部门查找所有价格本
     */
    List<PriceBook> findByDepartment(String department);
    
    /**
     * 根据产品类别查找价格本
     */
    List<PriceBook> findByProductType(String productType);
    
    /**
     * 根据产品类别和部门查找价格本
     */
    List<PriceBook> findByProductTypeAndDepartment(String productType, String department);
    
    /**
     * 检查是否存在相同产品ID和部门的价格本
     */
    boolean existsByProductIdAndDepartment(String productId, String department);
}

