package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesPerson;
import com.it.quanxianguanli.repository.SalesPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalesPersonService {

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    public List<SalesPerson> findAll() {
        return salesPersonRepository.findAll();
    }

    public Optional<SalesPerson> findById(String id) {
        return salesPersonRepository.findById(id);
    }

    public List<SalesPerson> findByRegionId(String regionId) {
        return salesPersonRepository.findByRegionId(regionId);
    }

    @Transactional
    public SalesPerson save(SalesPerson person) {
        // 如果提供了ERP同步ID，检查是否已存在
        if (person.getErpSyncId() != null && !person.getErpSyncId().isEmpty()) {
            Optional<SalesPerson> existing = salesPersonRepository.findByErpSyncId(person.getErpSyncId());
            if (existing.isPresent() && !existing.get().getId().equals(person.getId())) {
                throw new RuntimeException("该ERP同步ID已存在，营销人员信息需与信息化平台系统保持一致");
            }
        }
        return salesPersonRepository.save(person);
    }

    @Transactional
    public void delete(String id) {
        salesPersonRepository.deleteById(id);
    }

    @Transactional
    public void batchTransfer(List<String> personIds, String targetRegionId, String targetDepartment, String targetArea) {
        for (String personId : personIds) {
            Optional<SalesPerson> personOpt = salesPersonRepository.findById(personId);
            if (personOpt.isPresent()) {
                SalesPerson person = personOpt.get();
                person.setRegionId(targetRegionId);
                person.setDepartment(targetDepartment);
                person.setResponsibleArea(targetArea);
                salesPersonRepository.save(person);
            }
        }
    }
}

