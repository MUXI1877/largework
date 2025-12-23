package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SalesQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesQuotationRepository extends JpaRepository<SalesQuotation, String>, JpaSpecificationExecutor<SalesQuotation> {
    Optional<SalesQuotation> findByQuotationCode(String quotationCode);
}

