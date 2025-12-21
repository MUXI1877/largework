package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesPersonRepository extends JpaRepository<SalesPerson, String> {
    Optional<SalesPerson> findByEmployeeCode(String employeeCode);
    List<SalesPerson> findByRegionId(String regionId);
    Optional<SalesPerson> findByErpSyncId(String erpSyncId);
}

