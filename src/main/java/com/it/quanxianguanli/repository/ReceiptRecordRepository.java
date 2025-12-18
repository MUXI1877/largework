package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ReceiptRecord;
import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRecordRepository extends JpaRepository<ReceiptRecord, String> {
    List<ReceiptRecord> findByReceivable(Receivable receivable);
    List<ReceiptRecord> findByOperator(SysUser operator);
    List<ReceiptRecord> findByReceiptDateBetween(LocalDate startDate, LocalDate endDate);
    List<ReceiptRecord> findByReceiptMethod(String receiptMethod);
}