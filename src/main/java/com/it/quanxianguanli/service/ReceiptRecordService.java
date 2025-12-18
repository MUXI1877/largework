package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ReceiptRecord;
import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.repository.ReceiptRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptRecordService {

    @Autowired
    private ReceiptRecordRepository receiptRecordRepository;

    @Autowired
    private ReceivableService receivableService;

    @Transactional(readOnly = true)
    public List<ReceiptRecord> findAll() {
        return receiptRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ReceiptRecord> findById(String id) {
        return receiptRecordRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ReceiptRecord> findByReceivable(Receivable receivable) {
        return receiptRecordRepository.findByReceivable(receivable);
    }

    @Transactional(readOnly = true)
    public List<ReceiptRecord> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return receiptRecordRepository.findByReceiptDateBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ReceiptRecord> findByReceiptMethod(String receiptMethod) {
        return receiptRecordRepository.findByReceiptMethod(receiptMethod);
    }

    @Transactional
    public ReceiptRecord save(ReceiptRecord receiptRecord) {
        // 更新应收账的已收金额
        Receivable receivable = receiptRecord.getReceivable();
        receivableService.updateReceivedAmount(receivable.getId(), receiptRecord.getAmount().doubleValue());
        
        return receiptRecordRepository.save(receiptRecord);
    }

    @Transactional
    public void deleteById(String id) {
        receiptRecordRepository.deleteById(id);
    }

    @Transactional
    public ReceiptRecord update(ReceiptRecord receiptRecord) {
        Optional<ReceiptRecord> existing = receiptRecordRepository.findById(receiptRecord.getId());
        if (existing.isPresent()) {
            return receiptRecordRepository.save(receiptRecord);
        } else {
            throw new RuntimeException("收款记录不存在");
        }
    }

    @Transactional
    public List<ReceiptRecord> saveAll(List<ReceiptRecord> records) {
        for (ReceiptRecord record : records) {
            Receivable receivable = record.getReceivable();
            receivableService.updateReceivedAmount(receivable.getId(), record.getAmount().doubleValue());
        }
        return receiptRecordRepository.saveAll(records);
    }
}