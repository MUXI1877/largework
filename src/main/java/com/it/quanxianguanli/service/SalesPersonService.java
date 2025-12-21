package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SalesPerson;
import com.it.quanxianguanli.repository.SalesPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalesPersonService {

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    public List<SalesPerson> findAll() {
        return salesPersonRepository.findAll();
    }

    public SalesPerson findById(String id) {
        return salesPersonRepository.findById(id).orElse(null);
    }

    public List<SalesPerson> findByRegionId(String regionId) {
        return salesPersonRepository.findByRegionId(regionId);
    }

    @Transactional
    public SalesPerson save(SalesPerson salesPerson) {
        return salesPersonRepository.save(salesPerson);
    }

    @Transactional
    public void deleteById(String id) {
        salesPersonRepository.deleteById(id);
    }
}

