package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ReceivablePlan;
import com.it.quanxianguanli.entity.ReceivableReceipt;
import com.it.quanxianguanli.repository.ReceivablePlanRepository;
import com.it.quanxianguanli.repository.ReceivableReceiptRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceivableQueryService {

    private final ReceivablePlanRepository planRepository;
    private final ReceivableReceiptRepository receiptRepository;

    public ReceivableQueryService(ReceivablePlanRepository planRepository, ReceivableReceiptRepository receiptRepository) {
        this.planRepository = planRepository;
        this.receiptRepository = receiptRepository;
    }

    public Page<ReceivableSummary> query(String contractCode, String companyName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ReceivablePlan> plans = planRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (contractCode != null && !contractCode.isEmpty()) {
                predicates.add(cb.like(root.get("contractCode"), "%" + contractCode + "%"));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        Map<String, List<ReceivableReceipt>> receiptMap = receiptRepository.findAll().stream()
                .collect(Collectors.groupingBy(ReceivableReceipt::getContractCode));

        String companyFilter = companyName == null ? "" : companyName.trim().toLowerCase();

        List<ReceivableSummary> summaries = new ArrayList<>();
        for (ReceivablePlan plan : plans) {
            List<ReceivableReceipt> receipts = receiptMap.getOrDefault(plan.getContractCode(), Collections.emptyList());

            // 如果有公司名称过滤，只保留满足条件的回款记录；若全过滤掉则跳过该计划
            if (!companyFilter.isEmpty()) {
                receipts = receipts.stream()
                        .filter(r -> {
                            String cn = r.getCompanyName();
                            return cn != null && cn.toLowerCase().contains(companyFilter);
                        })
                        .collect(Collectors.toList());
                if (receipts.isEmpty()) {
                    continue;
                }
            }
            BigDecimal actualAmount = receipts.stream()
                    .map(ReceivableReceipt::getReceiveAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            LocalDate actualDate = receipts.stream()
                    .map(ReceivableReceipt::getReceiveDate)
                    .filter(Objects::nonNull)
                    .max(LocalDate::compareTo)
                    .orElse(null);

            ReceivableSummary dto = new ReceivableSummary();
            dto.setPlanId(plan.getId());
            dto.setContractCode(plan.getContractCode());
            dto.setContractName(plan.getContractName());
            dto.setItem(plan.getPaymentStageName());
            dto.setPlanAmount(plan.getDueAmount());
            dto.setPlanDate(plan.getDueDate());
            dto.setActualAmount(actualAmount);
            dto.setActualDate(actualDate);
            dto.setStatus(calculateStatus(plan.getDueAmount(), actualAmount, actualDate));
            dto.setDeviation(calcDeviation(plan.getDueAmount(), actualAmount));
            if (receipts != null && !receipts.isEmpty()) {
                ReceivableReceipt first = receipts.get(0);
                dto.setCompanyName(first.getCompanyName());
                dto.setCompanyCode(first.getCompanyCode());
            }
            summaries.add(dto);
        }

        // companyName 过滤
        // simple in-memory pagination
        int from = Math.min(page * size, summaries.size());
        int to = Math.min(from + size, summaries.size());
        List<ReceivableSummary> slice = summaries.subList(from, to);
        return new PageImpl<>(slice, pageable, summaries.size());
    }

    private String calculateStatus(BigDecimal planAmount, BigDecimal actualAmount, LocalDate actualDate) {
        BigDecimal due = planAmount == null ? BigDecimal.ZERO : planAmount;
        BigDecimal act = actualAmount == null ? BigDecimal.ZERO : actualAmount;
        if (act.compareTo(BigDecimal.ZERO) <= 0) return "未到款";
        if (act.compareTo(due) >= 0) return "已结清";
        return "部分到款";
    }

    private BigDecimal calcDeviation(BigDecimal planAmount, BigDecimal actualAmount) {
        BigDecimal due = planAmount == null ? BigDecimal.ZERO : planAmount;
        BigDecimal act = actualAmount == null ? BigDecimal.ZERO : actualAmount;
        return due.subtract(act);
    }

    public static class ReceivableSummary {
        private String planId;
        private String contractCode;
        private String contractName;
        private String companyName;
        private String companyCode;
        private String item;
        private BigDecimal planAmount;
        private BigDecimal actualAmount;
        private LocalDate planDate;
        private LocalDate actualDate;
        private String status;
        private BigDecimal deviation;

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getContractCode() {
            return contractCode;
        }

        public void setContractCode(String contractCode) {
            this.contractCode = contractCode;
        }

        public String getContractName() {
            return contractName;
        }

        public void setContractName(String contractName) {
            this.contractName = contractName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyCode() {
            return companyCode;
        }

        public void setCompanyCode(String companyCode) {
            this.companyCode = companyCode;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public BigDecimal getPlanAmount() {
            return planAmount;
        }

        public void setPlanAmount(BigDecimal planAmount) {
            this.planAmount = planAmount;
        }

        public BigDecimal getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(BigDecimal actualAmount) {
            this.actualAmount = actualAmount;
        }

        public LocalDate getPlanDate() {
            return planDate;
        }

        public void setPlanDate(LocalDate planDate) {
            this.planDate = planDate;
        }

        public LocalDate getActualDate() {
            return actualDate;
        }

        public void setActualDate(LocalDate actualDate) {
            this.actualDate = actualDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getDeviation() {
            return deviation;
        }

        public void setDeviation(BigDecimal deviation) {
            this.deviation = deviation;
        }
    }
}

