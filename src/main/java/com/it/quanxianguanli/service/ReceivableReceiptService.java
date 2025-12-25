package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ReceivablePlan;
import com.it.quanxianguanli.entity.ReceivableReceipt;
import com.it.quanxianguanli.repository.ReceivableReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivableReceiptService {

    private final ReceivableReceiptRepository receiptRepository;
    private final ReceivablePlanService planService;
    private final com.it.quanxianguanli.repository.ReceivablePlanRepository planRepository;

    public ReceivableReceiptService(ReceivableReceiptRepository receiptRepository, 
                                    ReceivablePlanService planService,
                                    com.it.quanxianguanli.repository.ReceivablePlanRepository planRepository) {
        this.receiptRepository = receiptRepository;
        this.planService = planService;
        this.planRepository = planRepository;
    }

    @Transactional
    public ReceivableReceipt create(ReceivableReceipt receipt) {
        if (receipt.getReceiveAmount() == null || receipt.getReceiveAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("到款金额必须大于0");
        }
        receipt.setRemainingAmount(calcRemaining(receipt.getContractCode(), receipt.getContractAmount(), receipt.getReceiveAmount()));
        ReceivableReceipt saved = receiptRepository.save(receipt);
        planService.applyReceipt(receipt.getContractCode(), receipt.getReceiveAmount(), receipt.getReceiveDate());
        return saved;
    }

    @Transactional
    public ReceivableReceipt updateRemark(String id, String remarks) {
        ReceivableReceipt receipt = receiptRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("回款记录不存在"));
        receipt.setRemarks(remarks);
        return receiptRepository.save(receipt);
    }

    public Page<ReceivableReceipt> page(String contractCode, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<ReceivableReceipt> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (contractCode != null && !contractCode.isEmpty()) {
                predicates.add(cb.like(root.get("contractCode"), "%" + contractCode + "%"));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receiveDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receiveDate"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return receiptRepository.findAll(spec, pageable);
    }

    @Transactional
    public void deleteById(String id) {
        ReceivableReceipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回款记录不存在"));
        
        String contractCode = receipt.getContractCode();
        
        // 删除回款记录
        receiptRepository.deleteById(id);
        
        // 重新计算该合同的应收账计划状态
        // 重置所有计划的已付金额
        List<ReceivablePlan> plans = planService.findByContractCodeOrderByDueDateAscCreateTimeAsc(contractCode);
        for (ReceivablePlan plan : plans) {
            plan.setPaidAmount(BigDecimal.ZERO);
            plan.setPaidDate(null);
            planService.updateStatus(plan);
            planRepository.save(plan);
        }
        
        // 重新分配所有剩余的回款记录
        List<ReceivableReceipt> allReceipts = receiptRepository.findByContractCode(contractCode);
        for (ReceivableReceipt r : allReceipts) {
            if (r.getReceiveAmount() != null && r.getReceiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                planService.applyReceipt(r.getContractCode(), r.getReceiveAmount(), r.getReceiveDate());
            }
        }
    }

    private BigDecimal calcRemaining(String contractCode, BigDecimal contractAmount, BigDecimal latestReceive) {
        BigDecimal totalReceived = receiptRepository.sumReceivedByContract(contractCode);
        BigDecimal base = contractAmount == null ? BigDecimal.ZERO : contractAmount;
        return base.subtract(totalReceived == null ? BigDecimal.ZERO : totalReceived).subtract(latestReceive == null ? BigDecimal.ZERO : latestReceive);
    }
}

