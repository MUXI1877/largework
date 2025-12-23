package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalesQuotationService {

    @Autowired
    private SalesQuotationRepository salesQuotationRepository;
    
    @Autowired
    private QuotationItemRepository quotationItemRepository;
    
    @Autowired
    private ProjectOpportunityRepository projectOpportunityRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    public List<SalesQuotation> findAll() {
        return salesQuotationRepository.findAll();
    }

    public List<SalesQuotation> findByConditions(String quotationName, String customerName, 
                                                 LocalDate startDate, LocalDate endDate) {
        Specification<SalesQuotation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (quotationName != null && !quotationName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("quotationName")), "%" + quotationName.toLowerCase() + "%"));
            }
            
            if (customerName != null && !customerName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%"));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("quotationDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("quotationDate"), endDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return salesQuotationRepository.findAll(spec);
    }

    public Optional<SalesQuotation> findById(String id) {
        return salesQuotationRepository.findById(id);
    }

    @Transactional
    public SalesQuotation save(SalesQuotation quotation) {
        // 生成报价编号
        if (quotation.getQuotationCode() == null || quotation.getQuotationCode().isEmpty()) {
            quotation.setQuotationCode("QUO" + System.currentTimeMillis());
        }
        
        // 如果关联了销售机会，同步机会信息
        if (quotation.getOpportunityId() != null && !quotation.getOpportunityId().isEmpty()) {
            Optional<ProjectOpportunity> oppOpt = projectOpportunityRepository.findById(quotation.getOpportunityId());
            if (oppOpt.isPresent()) {
                ProjectOpportunity opportunity = oppOpt.get();
                quotation.setOpportunityName(opportunity.getOpportunityName());
                quotation.setOpportunityCode(opportunity.getOpportunityCode());
                quotation.setStage(opportunity.getStage());
                quotation.setBudget(opportunity.getBudget());
                quotation.setIndustry(opportunity.getIndustry());
                
                // 同步客户信息
                if (opportunity.getCustomerId() != null) {
                    Optional<Customer> customerOpt = customerRepository.findById(opportunity.getCustomerId());
                    if (customerOpt.isPresent()) {
                        quotation.setCustomerId(opportunity.getCustomerId());
                        quotation.setCustomerName(customerOpt.get().getUnitName());
                    }
                }
            }
        }
        
        // 计算总价
        List<QuotationItem> items = quotationItemRepository.findByQuotationId(quotation.getId());
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (QuotationItem item : items) {
            if (item.getTotalPrice() != null) {
                totalPrice = totalPrice.add(item.getTotalPrice());
            }
        }
        quotation.setTotalPrice(totalPrice);
        
        return salesQuotationRepository.save(quotation);
    }

    @Transactional
    public void delete(String id) {
        // 删除报价明细
        quotationItemRepository.deleteByQuotationId(id);
        salesQuotationRepository.deleteById(id);
    }

    public List<QuotationItem> getItems(String quotationId) {
        return quotationItemRepository.findByQuotationId(quotationId);
    }

    @Transactional
    public void saveItems(String quotationId, List<QuotationItem> items) {
        // 删除原有明细
        quotationItemRepository.deleteByQuotationId(quotationId);
        
        // 保存新明细
        for (QuotationItem item : items) {
            item.setQuotationId(quotationId);
            
            // 如果关联了产品，同步产品信息
            if (item.getProductId() != null && !item.getProductId().isEmpty()) {
                Optional<Product> productOpt = productRepository.findById(item.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    item.setProductName(product.getName());
                    item.setDrawingNumber(product.getDrawingNumber());
                    item.setInventoryQuantity(product.getQuantity());
                    item.setIsStagnant(product.getIsStagnant());
                }
            }
            
            // 计算总价
            if (item.getUnitPrice() != null && item.getQuantity() != null) {
                item.setTotalPrice(item.getUnitPrice().multiply(item.getQuantity()));
            }
            
            quotationItemRepository.save(item);
        }
        
        // 更新报价单总价
        Optional<SalesQuotation> quotationOpt = salesQuotationRepository.findById(quotationId);
        if (quotationOpt.isPresent()) {
            SalesQuotation quotation = quotationOpt.get();
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (QuotationItem item : items) {
                if (item.getTotalPrice() != null) {
                    totalPrice = totalPrice.add(item.getTotalPrice());
                }
            }
            quotation.setTotalPrice(totalPrice);
            salesQuotationRepository.save(quotation);
        }
    }
}

