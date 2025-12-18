package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.entity.ServiceRecord;
import com.it.quanxianguanli.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, String> {
    List<ServiceRecord> findByService(ServiceOrder service);
    List<ServiceRecord> findByOperator(SysUser operator);
}