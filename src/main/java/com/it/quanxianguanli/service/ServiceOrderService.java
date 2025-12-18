package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.entity.SysUser;
import com.it.quanxianguanli.repository.ServiceOrderRepository;
import com.it.quanxianguanli.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;
    @Autowired
    private SysUserRepository sysUserRepository;

    @Transactional(readOnly = true)
    public List<ServiceOrder> findAll() {
        return serviceOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ServiceOrder> findById(String id) {
        return serviceOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ServiceOrder> findByOrderNo(String orderNo) {
        return serviceOrderRepository.findByOrderNo(orderNo);
    }

    @Transactional(readOnly = true)
    public List<ServiceOrder> findByStatus(String status) {
        return serviceOrderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<ServiceOrder> findByAssignTo(String userId) {
        Optional<SysUser> user = sysUserRepository.findById(userId);
        return user.map(serviceOrderRepository::findByAssignTo).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<ServiceOrder> findByCreateUser(String userId) {
        Optional<SysUser> user = sysUserRepository.findById(userId);
        return user.map(serviceOrderRepository::findByCreateUser).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public List<ServiceOrder> findByCustomerId(String customerId) {
        return serviceOrderRepository.findByCustomerId(customerId);
    }

    @Transactional
    public ServiceOrder save(ServiceOrder serviceOrder) {
        return serviceOrderRepository.save(serviceOrder);
    }

    @Transactional
    public void deleteById(String id) {
        serviceOrderRepository.deleteById(id);
    }

    @Transactional
    public ServiceOrder update(ServiceOrder serviceOrder) {
        Optional<ServiceOrder> existing = serviceOrderRepository.findById(serviceOrder.getId());
        if (existing.isPresent()) {
            return serviceOrderRepository.save(serviceOrder);
        } else {
            throw new RuntimeException("售后服务订单不存在");
        }
    }

    @Transactional
    public ServiceOrder updateStatus(String id, String status) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("售后服务订单不存在"));
        serviceOrder.setStatus(status);
        return serviceOrderRepository.save(serviceOrder);
    }

    @Transactional
    public ServiceOrder assignTo(String id, String userId) {
        ServiceOrder serviceOrder = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("售后服务订单不存在"));
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        serviceOrder.setAssignTo(user);
        return serviceOrderRepository.save(serviceOrder);
    }
}