package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ReceivablePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceivablePlanRepository extends JpaRepository<ReceivablePlan, String>, JpaSpecificationExecutor<ReceivablePlan> {

    List<ReceivablePlan> findByContractCodeOrderByDueDateAscCreateTimeAsc(String contractCode);
    
    // 检查合同号+付款阶段是否已存在
    boolean existsByContractCodeAndPaymentStage(String contractCode, String paymentStage);
    
    // 检查合同号+付款阶段名称是否已存在
    boolean existsByContractCodeAndPaymentStageName(String contractCode, String paymentStageName);
    
    // 检查合同号+付款阶段是否已存在（排除指定ID）
    @Query("SELECT COUNT(p) > 0 FROM ReceivablePlan p WHERE p.contractCode = :contractCode AND p.paymentStage = :paymentStage AND p.id != :excludeId")
    boolean existsByContractCodeAndPaymentStageExcludingId(@Param("contractCode") String contractCode, 
                                                             @Param("paymentStage") String paymentStage, 
                                                             @Param("excludeId") String excludeId);
    
    // 检查合同号+付款阶段名称是否已存在（排除指定ID）
    @Query("SELECT COUNT(p) > 0 FROM ReceivablePlan p WHERE p.contractCode = :contractCode AND p.paymentStageName = :paymentStageName AND p.id != :excludeId")
    boolean existsByContractCodeAndPaymentStageNameExcludingId(@Param("contractCode") String contractCode, 
                                                                 @Param("paymentStageName") String paymentStageName, 
                                                                 @Param("excludeId") String excludeId);

    /** 合同总应收金额（应付金额汇总） */
    @Query("SELECT COALESCE(SUM(p.dueAmount), 0) FROM ReceivablePlan p WHERE p.contractCode = :contractCode")
    java.math.BigDecimal sumDueAmountByContractCode(@Param("contractCode") String contractCode);

    /** 合同已收金额（计划表已付汇总，用于冗余校验） */
    @Query("SELECT COALESCE(SUM(p.paidAmount), 0) FROM ReceivablePlan p WHERE p.contractCode = :contractCode")
    java.math.BigDecimal sumPaidAmountByContractCode(@Param("contractCode") String contractCode);
}

