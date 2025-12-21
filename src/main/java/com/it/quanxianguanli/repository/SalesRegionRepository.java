package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesRegionRepository extends JpaRepository<SalesRegion, String> {
    Optional<SalesRegion> findByRegionCode(String regionCode);
}

