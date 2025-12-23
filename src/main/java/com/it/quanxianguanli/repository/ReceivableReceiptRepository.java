package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ReceivableReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReceivableReceiptRepository extends JpaRepository<ReceivableReceipt, String>, JpaSpecificationExecutor<ReceivableReceipt> {

    List<ReceivableReceipt> findByContractCodeOrderByReceiveDateAsc(String contractCode);

    @Query("select coalesce(sum(r.receiveAmount),0) from ReceivableReceipt r where r.contractCode = :contractCode")
    BigDecimal sumReceivedByContract(@Param("contractCode") String contractCode);
}

