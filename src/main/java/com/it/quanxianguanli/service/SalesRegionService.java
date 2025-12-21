package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesRegion;
import com.it.quanxianguanli.repository.SalesRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalesRegionService {

    @Autowired
    private SalesRegionRepository salesRegionRepository;

    public List<SalesRegion> findAll() {
        return salesRegionRepository.findAll();
    }

    public Optional<SalesRegion> findById(String id) {
        return salesRegionRepository.findById(id);
    }

    @Transactional
    public SalesRegion save(SalesRegion region) {
        // 片区编号唯一性检查
        if (region.getId() == null || region.getId().isEmpty()) {
            if (salesRegionRepository.findByRegionCode(region.getRegionCode()).isPresent()) {
                throw new RuntimeException("片区编号已存在");
            }
        } else {
            Optional<SalesRegion> existing = salesRegionRepository.findByRegionCode(region.getRegionCode());
            if (existing.isPresent() && !existing.get().getId().equals(region.getId())) {
                throw new RuntimeException("片区编号已存在");
            }
        }
        return salesRegionRepository.save(region);
    }

    @Transactional
    public void delete(String id) {
        salesRegionRepository.deleteById(id);
    }
}

