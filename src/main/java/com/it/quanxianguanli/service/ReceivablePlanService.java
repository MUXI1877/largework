package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ReceivablePlan;
import com.it.quanxianguanli.repository.ReceivablePlanRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivablePlanService {

    private final ReceivablePlanRepository planRepository;

    public ReceivablePlanService(ReceivablePlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Transactional
    public ReceivablePlan save(ReceivablePlan plan) {
        if (plan.getPaidAmount() == null) {
            plan.setPaidAmount(BigDecimal.ZERO);
        }
        updateStatus(plan);
        return planRepository.save(plan);
    }

    @Transactional
    public ReceivablePlan updateBasicInfo(String id, LocalDate dueDate, String owner, String remarks) {
        ReceivablePlan plan = planRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("计划不存在"));
        plan.setDueDate(dueDate);
        plan.setOwner(owner);
        plan.setRemarks(remarks);
        return planRepository.save(plan);
    }

    public Page<ReceivablePlan> page(String contractCode, String contractName, LocalDate startDueDate, LocalDate endDueDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<ReceivablePlan> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (contractCode != null && !contractCode.isEmpty()) {
                predicates.add(cb.like(root.get("contractCode"), "%" + contractCode + "%"));
            }
            if (contractName != null && !contractName.isEmpty()) {
                predicates.add(cb.like(root.get("contractName"), "%" + contractName + "%"));
            }
            if (startDueDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), startDueDate));
            }
            if (endDueDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), endDueDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return planRepository.findAll(spec, pageable);
    }

    /**
     * 核销回款到计划，按应付时间顺序分配
     */
    @Transactional
    public void applyReceipt(String contractCode, BigDecimal receiveAmount, LocalDate receiveDate) {
        if (receiveAmount == null || receiveAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        List<ReceivablePlan> plans = planRepository.findByContractCodeOrderByDueDateAscCreateTimeAsc(contractCode);
        BigDecimal remaining = receiveAmount;
        for (ReceivablePlan plan : plans) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            BigDecimal due = plan.getDueAmount() == null ? BigDecimal.ZERO : plan.getDueAmount();
            BigDecimal paid = plan.getPaidAmount() == null ? BigDecimal.ZERO : plan.getPaidAmount();
            BigDecimal gap = due.subtract(paid);
            if (gap.compareTo(BigDecimal.ZERO) <= 0) {
                updateStatus(plan);
                continue;
            }
            BigDecimal allocate = remaining.min(gap);
            plan.setPaidAmount(paid.add(allocate));
            if (plan.getPaidAmount().compareTo(due) >= 0) {
                plan.setPaidDate(receiveDate);
            }
            updateStatus(plan);
            planRepository.save(plan);
            remaining = remaining.subtract(allocate);
        }
    }

    private void updateStatus(ReceivablePlan plan) {
        BigDecimal due = plan.getDueAmount() == null ? BigDecimal.ZERO : plan.getDueAmount();
        BigDecimal paid = plan.getPaidAmount() == null ? BigDecimal.ZERO : plan.getPaidAmount();
        if (paid.compareTo(BigDecimal.ZERO) <= 0) {
            plan.setStatus("NOT_PAID");
        } else if (paid.compareTo(due) >= 0) {
            plan.setStatus("PAID");
        } else {
            plan.setStatus("PARTIAL");
        }
    }
}

