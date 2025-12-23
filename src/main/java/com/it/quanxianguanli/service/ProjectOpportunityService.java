package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectOpportunityService {

    @Autowired
    private ProjectOpportunityRepository projectOpportunityRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OpportunityRegionRepository opportunityRegionRepository;
    
    @Autowired
    private OpportunityEmployeeRepository opportunityEmployeeRepository;
    
    @Autowired
    private OpportunityKeyPersonRepository opportunityKeyPersonRepository;
    
    @Autowired
    private OpportunityTrackingRepository opportunityTrackingRepository;
    
    @Autowired
    private SalesRegionRepository salesRegionRepository;
    
    @Autowired
    private SalesPersonRepository salesPersonRepository;

    public List<ProjectOpportunity> findAll() {
        return projectOpportunityRepository.findAll();
    }

    public List<ProjectOpportunity> findByConditions(String opportunityName, LocalDate startDate, LocalDate endDate, 
                                                      String status, String source, String industry) {
        Specification<ProjectOpportunity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (opportunityName != null && !opportunityName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("opportunityName")), "%" + opportunityName.toLowerCase() + "%"));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("opportunityDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("opportunityDate"), endDate));
            }
            
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            if (source != null && !source.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("source"), source));
            }
            
            if (industry != null && !industry.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("industry"), industry));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return projectOpportunityRepository.findAll(spec);
    }

    public Optional<ProjectOpportunity> findById(String id) {
        return projectOpportunityRepository.findById(id);
    }

    @Transactional
    public ProjectOpportunity save(ProjectOpportunity opportunity) {
        // 生成机会编号
        if (opportunity.getOpportunityCode() == null || opportunity.getOpportunityCode().isEmpty()) {
            opportunity.setOpportunityCode("OPP" + System.currentTimeMillis());
        }
        
        // 如果选择了客户，自动关联客户信息
        if (opportunity.getCustomerId() != null && !opportunity.getCustomerId().isEmpty()) {
            Optional<Customer> customerOpt = customerRepository.findById(opportunity.getCustomerId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                opportunity.setCustomerName(customer.getUnitName());
                opportunity.setIndustry(customer.getIndustry());
            }
        }
        
        return projectOpportunityRepository.save(opportunity);
    }

    @Transactional
    public void delete(String id) {
        // 检查是否已提交
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(id);
        if (opt.isPresent() && opt.get().getIsSubmitted()) {
            throw new RuntimeException("已提交的销售机会不可删除");
        }
        
        // 删除关联数据
        List<OpportunityTracking> trackings = opportunityTrackingRepository.findByOpportunityId(id);
        for (OpportunityTracking tracking : trackings) {
            opportunityTrackingRepository.deleteById(tracking.getId());
        }
        opportunityRegionRepository.deleteByOpportunityId(id);
        opportunityEmployeeRepository.deleteByOpportunityId(id);
        opportunityKeyPersonRepository.deleteByOpportunityId(id);
        
        projectOpportunityRepository.deleteById(id);
    }

    @Transactional
    public void submit(String id) {
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(id);
        if (opt.isPresent()) {
            ProjectOpportunity opportunity = opt.get();
            opportunity.setIsSubmitted(true);
            opportunity.setStatus("已提交");
            projectOpportunityRepository.save(opportunity);
        } else {
            throw new RuntimeException("销售机会不存在");
        }
    }

    @Transactional
    public void transferRegions(String opportunityId, List<String> regionIds) {
        // 删除原有片区关联
        opportunityRegionRepository.deleteByOpportunityId(opportunityId);
        
        // 添加新片区关联
        for (String regionId : regionIds) {
            Optional<SalesRegion> regionOpt = salesRegionRepository.findById(regionId);
            if (regionOpt.isPresent()) {
                OpportunityRegion or = new OpportunityRegion();
                or.setOpportunityId(opportunityId);
                or.setRegionId(regionId);
                or.setRegionName(regionOpt.get().getRegionName());
                opportunityRegionRepository.save(or);
            }
        }
    }

    @Transactional
    public void assignEmployees(String opportunityId, List<String> employeeIds) {
        // 删除原有员工关联
        opportunityEmployeeRepository.deleteByOpportunityId(opportunityId);
        
        // 添加新员工关联
        for (String employeeId : employeeIds) {
            Optional<SalesPerson> employeeOpt = salesPersonRepository.findById(employeeId);
            if (employeeOpt.isPresent()) {
                OpportunityEmployee oe = new OpportunityEmployee();
                oe.setOpportunityId(opportunityId);
                oe.setEmployeeId(employeeId);
                oe.setEmployeeName(employeeOpt.get().getName());
                opportunityEmployeeRepository.save(oe);
            }
        }
    }

    @Transactional
    public void close(String opportunityId, String closeReason) {
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(opportunityId);
        if (opt.isPresent()) {
            ProjectOpportunity opportunity = opt.get();
            opportunity.setStatus("已关闭");
            opportunity.setRemarks((opportunity.getRemarks() != null ? opportunity.getRemarks() + "\n" : "") + 
                                   "关闭原因：" + closeReason);
            projectOpportunityRepository.save(opportunity);
        } else {
            throw new RuntimeException("销售机会不存在");
        }
    }

    public List<OpportunityTracking> getTrackings(String opportunityId) {
        return opportunityTrackingRepository.findByOpportunityIdOrderByCreateTimeDesc(opportunityId);
    }

    @Transactional
    public OpportunityTracking saveTracking(OpportunityTracking tracking) {
        // 从销售机会中同步基本信息
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(tracking.getOpportunityId());
        if (opt.isPresent()) {
            ProjectOpportunity opportunity = opt.get();
            tracking.setOpportunityName(opportunity.getOpportunityName());
            tracking.setStage(opportunity.getStage());
            tracking.setCustomerId(opportunity.getCustomerId());
            tracking.setCustomerName(opportunity.getCustomerName());
            tracking.setBudget(opportunity.getBudget());
            tracking.setIndustry(opportunity.getIndustry());
            tracking.setSource(opportunity.getSource());
            tracking.setOpportunityDate(opportunity.getOpportunityDate());
        }
        return opportunityTrackingRepository.save(tracking);
    }

    @Transactional
    public void deleteTracking(String trackingId) {
        opportunityTrackingRepository.deleteById(trackingId);
    }

    public List<OpportunityRegion> getRegions(String opportunityId) {
        return opportunityRegionRepository.findByOpportunityId(opportunityId);
    }

    public List<OpportunityEmployee> getEmployees(String opportunityId) {
        return opportunityEmployeeRepository.findByOpportunityId(opportunityId);
    }

    public List<OpportunityKeyPerson> getKeyPersons(String opportunityId) {
        return opportunityKeyPersonRepository.findByOpportunityId(opportunityId);
    }

    @Transactional
    public void saveKeyPersons(String opportunityId, List<OpportunityKeyPerson> keyPersons) {
        // 删除原有关键人物
        opportunityKeyPersonRepository.deleteByOpportunityId(opportunityId);
        
        // 保存新关键人物
        for (OpportunityKeyPerson kp : keyPersons) {
            kp.setOpportunityId(opportunityId);
            opportunityKeyPersonRepository.save(kp);
        }
    }
}

