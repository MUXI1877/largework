package com.it.quanxianguanli.service;

import com.it.quanxianguanli.dto.SalesPersonDTO;
import com.it.quanxianguanli.entity.SalesPerson;
import com.it.quanxianguanli.repository.SalesPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalesPersonService {

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    @Autowired
    private SalesRegionService salesRegionService;

    // 返回包含片区名称的DTO列表
    public List<SalesPersonDTO> findAllWithRegionName() {
        return salesPersonRepository.findAll().stream()
                .map(this::convertToDTOWithRegionName)
                .collect(Collectors.toList());
    }

    // 根据ID返回包含片区名称的DTO
    public Optional<SalesPersonDTO> findByIdWithRegionName(String id) {
        return salesPersonRepository.findById(id)
                .map(this::convertToDTOWithRegionName);
    }

    // 根据片区ID返回包含片区名称的DTO列表
    public List<SalesPersonDTO> findByRegionIdWithRegionName(String regionId) {
        return salesPersonRepository.findByRegionId(regionId).stream()
                .map(this::convertToDTOWithRegionName)
                .collect(Collectors.toList());
    }

    // 将SalesPerson转换为SalesPersonDTO并填充片区名称
    private SalesPersonDTO convertToDTOWithRegionName(SalesPerson person) {
        SalesPersonDTO dto = new SalesPersonDTO();
        dto.setId(person.getId());
        dto.setEmployeeCode(person.getEmployeeCode());
        dto.setName(person.getName());
        dto.setGender(person.getGender());
        dto.setBirthday(person.getBirthday());
        dto.setContactInfo(person.getContactInfo());
        dto.setRegionId(person.getRegionId());
        dto.setPosition(person.getPosition());
        dto.setDepartment(person.getDepartment());
        dto.setResponsibleArea(person.getResponsibleArea());
        dto.setRemarks(person.getRemarks());
        dto.setErpSyncId(person.getErpSyncId());

        if (person.getRegionId() != null) {
            salesRegionService.findById(person.getRegionId())
                    .ifPresent(region -> dto.setRegionName(region.getRegionName()));
        }
        return dto;
    }

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