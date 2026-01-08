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
        
        // 如果 paymentStage 为空但 paymentStageName 不为空，将 paymentStageName 赋值给 paymentStage
        // 这样确保 paymentStage 有值，用于唯一性校验
        if ((plan.getPaymentStage() == null || plan.getPaymentStage().isEmpty()) && 
            plan.getPaymentStageName() != null && !plan.getPaymentStageName().isEmpty()) {
            plan.setPaymentStage(plan.getPaymentStageName());
        }
        
        // 如果 paymentStageName 为空但 paymentStage 不为空，将 paymentStage 赋值给 paymentStageName
        if ((plan.getPaymentStageName() == null || plan.getPaymentStageName().isEmpty()) && 
            plan.getPaymentStage() != null && !plan.getPaymentStage().isEmpty()) {
            plan.setPaymentStageName(plan.getPaymentStage());
        }
        
        // 检查合同号+付款阶段的唯一性
        // 优先使用 paymentStage，如果为空则使用 paymentStageName
        String paymentStage = plan.getPaymentStage();
        String paymentStageName = plan.getPaymentStageName();
        
        // 确定用于唯一性检查的值
        String checkValue = null;
        if (paymentStage != null && !paymentStage.isEmpty()) {
            checkValue = paymentStage;
        } else if (paymentStageName != null && !paymentStageName.isEmpty()) {
            checkValue = paymentStageName;
        }
        
        // 如果是新增（id为空），检查是否已存在
        // 如果是修改（id不为空），检查除当前记录外是否已存在
        if (checkValue != null && !checkValue.isEmpty() && plan.getContractCode() != null && !plan.getContractCode().isEmpty()) {
            boolean exists = false;
            if (plan.getId() == null || plan.getId().isEmpty()) {
                // 新增：检查是否已存在相同的合同号+付款阶段（或付款阶段名称）
                try {
                    exists = planRepository.existsByContractCodeAndPaymentStage(plan.getContractCode(), checkValue) ||
                             planRepository.existsByContractCodeAndPaymentStageName(plan.getContractCode(), checkValue);
                } catch (Exception e) {
                    System.err.println("检查唯一性时出错: " + e.getMessage());
                    e.printStackTrace();
                    // 如果查询出错，不阻止保存，但记录错误
                }
            } else {
                // 修改：检查除当前记录外是否已存在相同的合同号+付款阶段（或付款阶段名称）
                try {
                    exists = planRepository.existsByContractCodeAndPaymentStageExcludingId(plan.getContractCode(), checkValue, plan.getId()) ||
                             planRepository.existsByContractCodeAndPaymentStageNameExcludingId(plan.getContractCode(), checkValue, plan.getId());
                } catch (Exception e) {
                    System.err.println("检查唯一性时出错: " + e.getMessage());
                    e.printStackTrace();
                    // 如果查询出错，不阻止保存，但记录错误
                }
            }
            
            if (exists) {
                throw new IllegalArgumentException("该合同号[" + plan.getContractCode() + "]的付款阶段[" + 
                    (paymentStageName != null ? paymentStageName : 
                     paymentStage != null ? paymentStage : checkValue != null ? checkValue : "") + "]已存在，不能重复添加");
            }
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

    @Transactional
    public ReceivablePlan update(String id, ReceivablePlan req) {
        ReceivablePlan plan = planRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("计划不存在"));
        
        // 同步字段
        if ((req.getPaymentStage() == null || req.getPaymentStage().isEmpty()) && 
            req.getPaymentStageName() != null && !req.getPaymentStageName().isEmpty()) {
            req.setPaymentStage(req.getPaymentStageName());
        }
        if ((req.getPaymentStageName() == null || req.getPaymentStageName().isEmpty()) && 
            req.getPaymentStage() != null && !req.getPaymentStage().isEmpty()) {
            req.setPaymentStageName(req.getPaymentStage());
        }
        
        // 如果合同号或付款阶段有变化，需要检查唯一性
        String newContractCode = req.getContractCode() != null ? req.getContractCode() : plan.getContractCode();
        String newPaymentStage = req.getPaymentStage() != null ? req.getPaymentStage() : plan.getPaymentStage();
        String newPaymentStageName = req.getPaymentStageName() != null ? req.getPaymentStageName() : plan.getPaymentStageName();
        
        // 确定用于唯一性检查的值
        String checkValue = null;
        if (newPaymentStage != null && !newPaymentStage.isEmpty()) {
            checkValue = newPaymentStage;
        } else if (newPaymentStageName != null && !newPaymentStageName.isEmpty()) {
            checkValue = newPaymentStageName;
        }
        
        // 检查除当前记录外是否已存在相同的合同号+付款阶段
        if (checkValue != null && !checkValue.isEmpty() && 
            (!newContractCode.equals(plan.getContractCode()) || 
             (newPaymentStage != null && !newPaymentStage.equals(plan.getPaymentStage())) ||
             (newPaymentStageName != null && !newPaymentStageName.equals(plan.getPaymentStageName())))) {
            boolean exists = planRepository.existsByContractCodeAndPaymentStageExcludingId(newContractCode, checkValue, id) ||
                             planRepository.existsByContractCodeAndPaymentStageNameExcludingId(newContractCode, checkValue, id);
            if (exists) {
                throw new IllegalArgumentException("该合同号[" + newContractCode + "]的付款阶段[" + 
                    (newPaymentStageName != null ? newPaymentStageName : 
                     newPaymentStage != null ? newPaymentStage : checkValue) + "]已存在，不能重复添加");
            }
        }
        
        // 更新所有可修改的字段（除了已付金额和状态，这些由回款逻辑控制）
        if (req.getContractCode() != null) {
            plan.setContractCode(req.getContractCode());
        }
        if (req.getContractName() != null) {
            plan.setContractName(req.getContractName());
        }
        if (req.getPaymentStage() != null) {
            plan.setPaymentStage(req.getPaymentStage());
        }
        if (req.getPaymentStageName() != null) {
            plan.setPaymentStageName(req.getPaymentStageName());
        }
        if (req.getDueAmount() != null) {
            plan.setDueAmount(req.getDueAmount());
        }
        if (req.getDueDate() != null) {
            plan.setDueDate(req.getDueDate());
        }
        if (req.getOwner() != null) {
            plan.setOwner(req.getOwner());
        }
        if (req.getRemarks() != null) {
            plan.setRemarks(req.getRemarks());
        }
        // 允许修改已付金额和付款日期（用于手动调整）
        if (req.getPaidAmount() != null) {
            plan.setPaidAmount(req.getPaidAmount());
        }
        if (req.getPaidDate() != null) {
            plan.setPaidDate(req.getPaidDate());
        }
        // 重新计算状态（基于已付金额和应付金额）
        updateStatus(plan);
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

    public List<ReceivablePlan> findByContractCodeOrderByDueDateAscCreateTimeAsc(String contractCode) {
        return planRepository.findByContractCodeOrderByDueDateAscCreateTimeAsc(contractCode);
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

    @Transactional
    public void deleteById(String id) {
        planRepository.deleteById(id);
    }

    public void updateStatus(ReceivablePlan plan) {
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

