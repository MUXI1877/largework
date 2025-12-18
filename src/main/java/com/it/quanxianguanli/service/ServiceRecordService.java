package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.entity.ServiceRecord;
import com.it.quanxianguanli.repository.ServiceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceRecordService {

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Transactional(readOnly = true)
    public List<ServiceRecord> findAll() {
        return serviceRecordRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ServiceRecord> findById(String id) {
        return serviceRecordRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ServiceRecord> findByService(ServiceOrder service) {
        return serviceRecordRepository.findByService(service);
    }

    @Transactional(readOnly = true)
    public List<ServiceRecord> findByOperator(com.it.quanxianguanli.entity.SysUser operator) {
        return serviceRecordRepository.findByOperator(operator);
    }

    @Transactional
    public ServiceRecord save(ServiceRecord serviceRecord) {
        return serviceRecordRepository.save(serviceRecord);
    }

    @Transactional
    public void deleteById(String id) {
        serviceRecordRepository.deleteById(id);
    }

    @Transactional
    public ServiceRecord update(ServiceRecord serviceRecord) {
        Optional<ServiceRecord> existing = serviceRecordRepository.findById(serviceRecord.getId());
        if (existing.isPresent()) {
            return serviceRecordRepository.save(serviceRecord);
        } else {
            throw new RuntimeException("服务记录不存在");
        }
    }

    @Transactional
    public List<ServiceRecord> saveAll(List<ServiceRecord> records) {
        return serviceRecordRepository.saveAll(records);
    }
}