package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerKeyPersonRepository keyPersonRepository;
    
    @Autowired
    private ProjectOpportunityRepository projectOpportunityRepository;
    
    @Autowired
    private ContractRepository contractRepository;
    
    @Autowired
    private AfterSalesRepository afterSalesRepository;
    
    @Autowired
    private CustomerVisitRepository customerVisitRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Customer save(Customer customer) {
        // 处理空字符串ID，将其设置为null以便JPA正确识别为新增
        if (customer.getId() != null && customer.getId().trim().isEmpty()) {
            customer.setId(null);
        }
        
        boolean isNew = customer.getId() == null || customer.getId().trim().isEmpty();
        
        // 客户编号唯一性检查
        if (customer.getCustomerCode() != null && !customer.getCustomerCode().trim().isEmpty()) {
            Optional<Customer> existingByCode = customerRepository.findByCustomerCode(customer.getCustomerCode());
            if (isNew) {
                // 新增
                if (existingByCode.isPresent()) {
                    throw new RuntimeException("客户编号已存在，不能重复录入");
                }
            } else {
                // 修改
                if (existingByCode.isPresent() && !existingByCode.get().getId().equals(customer.getId())) {
                    throw new RuntimeException("客户编号已存在，不能重复录入");
                }
            }
        }
        
        // 客户名称唯一性检查
        if (customer.getUnitName() != null && !customer.getUnitName().trim().isEmpty()) {
            Optional<Customer> existingByName = customerRepository.findByUnitName(customer.getUnitName());
            if (isNew) {
                // 新增
                if (existingByName.isPresent()) {
                    throw new RuntimeException("客户名称已存在，不能重复录入");
                }
            } else {
                // 修改
                if (existingByName.isPresent() && !existingByName.get().getId().equals(customer.getId())) {
                    throw new RuntimeException("客户名称已存在，不能重复录入");
                }
            }
        }
        
        // 保存客户信息
        Customer saved = customerRepository.save(customer);
        
        // 确保数据已刷新到数据库
        customerRepository.flush();
        
        return saved;
    }

    @Transactional
    public void delete(String id) {
        // 检查关联信息
        long keyPersonCount = keyPersonRepository.findByCustomerId(id).size();
        long projectCount = projectOpportunityRepository.findByCustomerId(id).size();
        long contractCount = contractRepository.findByCustomerId(id).size();
        long afterSalesCount = afterSalesRepository.findByCustomerId(id).size();
        long visitCount = customerVisitRepository.findByCustomerId(id).size();
        
        if (keyPersonCount > 0 || projectCount > 0 || contractCount > 0 || 
            afterSalesCount > 0 || visitCount > 0) {
            throw new RuntimeException("该客户存在关联信息，无法删除。请先处理关联的关键人物、项目机会、合同、售后或来访信息");
        }
        
        customerRepository.deleteById(id);
    }

    // 客户关键人物管理
    public List<CustomerKeyPerson> getKeyPersons(String customerId) {
        return keyPersonRepository.findByCustomerId(customerId);
    }

    @Transactional
    public CustomerKeyPerson saveKeyPerson(CustomerKeyPerson keyPerson) {
        return keyPersonRepository.save(keyPerson);
    }

    @Transactional
    public void deleteKeyPerson(String id) {
        keyPersonRepository.deleteById(id);
    }

    // 项目机会管理
    public List<ProjectOpportunity> getProjectOpportunities(String customerId) {
        return projectOpportunityRepository.findByCustomerId(customerId);
    }

    // 合同信息管理
    public List<Contract> getContracts(String customerId) {
        return contractRepository.findByCustomerId(customerId);
    }

    // 售后信息管理
    public List<AfterSales> getAfterSales(String customerId) {
        return afterSalesRepository.findByCustomerId(customerId);
    }

    // 客户来访信息管理
    public List<CustomerVisit> getCustomerVisits(String customerId) {
        return customerVisitRepository.findByCustomerId(customerId);
    }
}

