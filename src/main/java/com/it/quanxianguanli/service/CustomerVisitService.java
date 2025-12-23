package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Customer;
import com.it.quanxianguanli.entity.CustomerVisit;
import com.it.quanxianguanli.repository.CustomerRepository;
import com.it.quanxianguanli.repository.CustomerVisitRepository;
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
public class CustomerVisitService {

    @Autowired
    private CustomerVisitRepository customerVisitRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerVisit> findAll() {
        return customerVisitRepository.findAll();
    }

    public List<CustomerVisit> findByConditions(String customerName, LocalDate startDate, LocalDate endDate, String status) {
        Specification<CustomerVisit> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (customerName != null && !customerName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%"));
            }
            
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("visitDate"), startDate));
            }
            
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("visitDate"), endDate));
            }
            
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return customerVisitRepository.findAll(spec);
    }

    public Optional<CustomerVisit> findById(String id) {
        return customerVisitRepository.findById(id);
    }

    @Transactional
    public CustomerVisit save(CustomerVisit visit) {
        // 如果选择了客户，自动关联客户信息
        if (visit.getCustomerId() != null && !visit.getCustomerId().isEmpty()) {
            Optional<Customer> customerOpt = customerRepository.findById(visit.getCustomerId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                visit.setCustomerName(customer.getUnitName());
                visit.setCustomerSequence(customer.getCustomerCode());
            }
        }
        return customerVisitRepository.save(visit);
    }

    @Transactional
    public void delete(String id) {
        customerVisitRepository.deleteById(id);
    }
}

