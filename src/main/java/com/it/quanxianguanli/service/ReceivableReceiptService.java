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
        // 合同总额 = 所有应收计划的应付金额汇总
        BigDecimal contractTotal = planRepository.sumDueAmountByContractCode(receipt.getContractCode());
        if (contractTotal == null || contractTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("未找到该合同的应收账计划或应付金额为0，无法登记回款");
        }

        // 已收款 = 现有回款记录汇总
        BigDecimal alreadyReceived = receiptRepository.sumReceivedByContract(receipt.getContractCode());
        if (alreadyReceived == null) {
            alreadyReceived = BigDecimal.ZERO;
        }

        // 校验是否超出应收总额
        BigDecimal afterReceive = alreadyReceived.add(receipt.getReceiveAmount());
        if (afterReceive.compareTo(contractTotal) > 0) {
            BigDecimal remaining = contractTotal.subtract(alreadyReceived);
            throw new IllegalArgumentException("到款金额超出应收金额，剩余额度：" + remaining.max(BigDecimal.ZERO));
        }

        // 设置合同总额与剩余金额
        receipt.setContractAmount(contractTotal);
        receipt.setRemainingAmount(contractTotal.subtract(afterReceive).max(BigDecimal.ZERO));

        ReceivableReceipt saved = receiptRepository.save(receipt);
        planService.applyReceipt(receipt.getContractCode(), receipt.getReceiveAmount(), receipt.getReceiveDate());
        return saved;
    }

    @Transactional
    public ReceivableReceipt update(String id, ReceivableReceipt req) {
        ReceivableReceipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("回款记录不存在"));
        
        String contractCode = receipt.getContractCode();
        BigDecimal oldAmount = receipt.getReceiveAmount() != null ? receipt.getReceiveAmount() : BigDecimal.ZERO;
        
        // 更新到款金额
        if (req.getReceiveAmount() != null && req.getReceiveAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newAmount = req.getReceiveAmount();
            
            // 如果金额有变化，需要重新校验和分配
            if (newAmount.compareTo(oldAmount) != 0) {
                // 先删除旧的回款分配（重置计划的已付金额）
                List<ReceivablePlan> plans = planService.findByContractCodeOrderByDueDateAscCreateTimeAsc(contractCode);
                for (ReceivablePlan plan : plans) {
                    plan.setPaidAmount(BigDecimal.ZERO);
                    plan.setPaidDate(null);
                    planService.updateStatus(plan);
                    planRepository.save(plan);
                }
                
                // 重新计算所有回款记录（排除当前记录）
                List<ReceivableReceipt> allReceipts = receiptRepository.findByContractCode(contractCode);
                for (ReceivableReceipt r : allReceipts) {
                    if (r.getId() != null && !r.getId().isEmpty() && !r.getId().equals(id)
                            && r.getReceiveAmount() != null && r.getReceiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                        planService.applyReceipt(r.getContractCode(), r.getReceiveAmount(), r.getReceiveDate());
                    }
                }
                
                // 校验新金额是否超出应收总额
                BigDecimal contractTotal = planRepository.sumDueAmountByContractCode(contractCode);
                if (contractTotal == null || contractTotal.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("未找到该合同的应收账计划或应付金额为0");
                }
                
                BigDecimal alreadyReceived = receiptRepository.sumReceivedByContract(contractCode);
                if (alreadyReceived == null) {
                    alreadyReceived = BigDecimal.ZERO;
                }
                // 减去旧金额，加上新金额
                BigDecimal afterReceive = alreadyReceived.subtract(oldAmount).add(newAmount);
                if (afterReceive.compareTo(contractTotal) > 0) {
                    BigDecimal remaining = contractTotal.subtract(alreadyReceived.subtract(oldAmount));
                    throw new IllegalArgumentException("到款金额超出应收金额，剩余额度：" + remaining.max(BigDecimal.ZERO));
                }
                
                receipt.setReceiveAmount(newAmount);
                
                // 重新分配新金额
                planService.applyReceipt(contractCode, newAmount, receipt.getReceiveDate() != null ? receipt.getReceiveDate() : req.getReceiveDate());
                
                // 更新剩余金额
                BigDecimal finalReceived = receiptRepository.sumReceivedByContract(contractCode);
                receipt.setRemainingAmount(contractTotal.subtract(finalReceived != null ? finalReceived : BigDecimal.ZERO).max(BigDecimal.ZERO));
            }
        }
        
        // 更新到款日期
        if (req.getReceiveDate() != null) {
            receipt.setReceiveDate(req.getReceiveDate());
        }
        
        // 更新备注
        if (req.getRemarks() != null) {
            receipt.setRemarks(req.getRemarks());
        }
        
        return receiptRepository.save(receipt);
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
            // 过滤掉id为null或空的记录
            predicates.add(cb.isNotNull(root.get("id")));
            predicates.add(cb.notEqual(root.get("id"), ""));
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

        Page<ReceivableReceipt> result = receiptRepository.findAll(spec, pageable);

        // 需求：同一合同的所有记录显示同一个“当前剩余金额”
        // 这里不再直接使用数据库中保存的 remainingAmount，而是按当前所有回款动态计算：
        // 当前剩余 = 合同总额(应收计划汇总) - 当前合同的所有回款总额
        List<ReceivableReceipt> content = result.getContent();
        if (!content.isEmpty()) {
            // 先收集本页涉及到的合同号，避免 N+1 查询
            java.util.Set<String> contractCodes = new java.util.HashSet<>();
            for (ReceivableReceipt r : content) {
                if (r.getContractCode() != null && !r.getContractCode().isEmpty()) {
                    contractCodes.add(r.getContractCode());
                }
            }

            for (String code : contractCodes) {
                // 合同总额：所有应收计划的应付金额汇总
                BigDecimal contractTotal = planRepository.sumDueAmountByContractCode(code);
                if (contractTotal == null) {
                    contractTotal = BigDecimal.ZERO;
                }
                // 当前合同总回款
                BigDecimal received = receiptRepository.sumReceivedByContract(code);
                if (received == null) {
                    received = BigDecimal.ZERO;
                }
                BigDecimal remaining = contractTotal.subtract(received).max(BigDecimal.ZERO);

                // 覆盖本页中该合同号所有记录的 remainingAmount，使其显示相同的“当前剩余金额”
                for (ReceivableReceipt r : content) {
                    if (code.equals(r.getContractCode())) {
                        r.setRemainingAmount(remaining);
                    }
                }
            }
        }

        return result;
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
            // 只处理有id的记录
            if (r.getId() != null && !r.getId().isEmpty() 
                    && r.getReceiveAmount() != null && r.getReceiveAmount().compareTo(BigDecimal.ZERO) > 0) {
                planService.applyReceipt(r.getContractCode(), r.getReceiveAmount(), r.getReceiveDate());
            }
        }
    }

    /**
     * 根据合同号查询合同信息（合同名称和合同总额）
     * 合同总额从应收账计划中汇总所有应付金额
     */
    public ContractInfo getContractInfo(String contractCode) {
        if (contractCode == null || contractCode.trim().isEmpty()) {
            throw new IllegalArgumentException("合同号不能为空");
        }
        
        List<ReceivablePlan> plans = planRepository.findByContractCodeOrderByDueDateAscCreateTimeAsc(contractCode);
        if (plans.isEmpty()) {
            throw new IllegalArgumentException("未找到合同号为[" + contractCode + "]的应收账计划");
        }
        
        // 从第一个计划中获取合同名称
        String contractName = plans.get(0).getContractName();
        if (contractName == null || contractName.trim().isEmpty()) {
            throw new IllegalArgumentException("合同号[" + contractCode + "]的合同名称为空");
        }
        
        // 汇总所有计划的应付金额作为合同总额
        BigDecimal contractAmount = plans.stream()
                .map(ReceivablePlan::getDueAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        ContractInfo info = new ContractInfo();
        info.setContractCode(contractCode);
        info.setContractName(contractName);
        info.setContractAmount(contractAmount);
        
        return info;
    }
    
    public static class ContractInfo {
        private String contractCode;
        private String contractName;
        private BigDecimal contractAmount;

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

        public BigDecimal getContractAmount() {
            return contractAmount;
        }

        public void setContractAmount(BigDecimal contractAmount) {
            this.contractAmount = contractAmount;
        }
    }

    private BigDecimal calcRemaining(String contractCode, BigDecimal contractAmount, BigDecimal latestReceive) {
        BigDecimal totalReceived = receiptRepository.sumReceivedByContract(contractCode);
        BigDecimal base = contractAmount == null ? BigDecimal.ZERO : contractAmount;
        return base.subtract(totalReceived == null ? BigDecimal.ZERO : totalReceived).subtract(latestReceive == null ? BigDecimal.ZERO : latestReceive);
    }
}

