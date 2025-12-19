package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.repository.ReceivableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReceivableService {

    @Autowired
    private ReceivableRepository receivableRepository;

    @Transactional(readOnly = true)
    public List<Receivable> findAll() {
        return receivableRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Receivable> findById(String id) {
        return receivableRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Receivable> findByOrder(SalesOrder order) {
        return receivableRepository.findByOrder(order);
    }

    @Transactional(readOnly = true)
    public List<Receivable> findByStatus(String status) {
        return receivableRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Receivable> findOverdue(LocalDate currentDate) {
        return receivableRepository.findByDueDateBefore(currentDate);
    }

    @Transactional(readOnly = true)
    public List<Receivable> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return receivableRepository.findByDueDateBetween(startDate, endDate);
    }

    @Transactional
    public Receivable save(Receivable receivable) {
        return receivableRepository.save(receivable);
    }

    @Transactional
    public void deleteById(String id) {
        receivableRepository.deleteById(id);
    }

    @Transactional
    public Receivable update(Receivable receivable) {
        Optional<Receivable> existing = receivableRepository.findById(receivable.getId());
        if (existing.isPresent()) {
            return receivableRepository.save(receivable);
        } else {
            throw new RuntimeException("应收账不存在");
        }
    }

    @Transactional
    public Receivable updateStatus(String id, String status) {
        Receivable receivable = receivableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("应收账不存在"));
        receivable.setStatus(status);
        return receivableRepository.save(receivable);
    }

    @Transactional
    public Receivable updateReceivedAmount(String id, double amount) {
        Receivable receivable = receivableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("应收账不存在"));

        // 确保已收金额非空，避免 NPE
        BigDecimal currentReceived = receivable.getReceivedAmount();
        if (currentReceived == null) {
            currentReceived = BigDecimal.ZERO;
        }

        BigDecimal increment = BigDecimal.valueOf(amount);
        BigDecimal newReceivedAmount = currentReceived.add(increment);
        receivable.setReceivedAmount(newReceivedAmount);
        
        // 使用 BigDecimal 比较金额，避免 double 精度问题
        BigDecimal totalAmount = receivable.getAmount();
        if (totalAmount != null) {
            int cmp = newReceivedAmount.compareTo(totalAmount);
            if (cmp >= 0) {
                // 已收金额达到或超过应收金额，标记为已结清
            receivable.setStatus("已结清");
            } else if (newReceivedAmount.compareTo(BigDecimal.ZERO) > 0) {
                // 已有部分收款
            receivable.setStatus("部分结清");
            }
        }
        
        return receivableRepository.save(receivable);
    }
}