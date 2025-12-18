package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, String> {
    Optional<ServiceOrder> findByOrderNo(String orderNo);
    List<ServiceOrder> findByStatus(String status);
    List<ServiceOrder> findByAssignTo(SysUser assignTo);
    List<ServiceOrder> findByCreateUser(SysUser createUser);
    List<ServiceOrder> findByCustomerId(String customerId);
}