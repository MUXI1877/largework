package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, String> {
    List<QuotationItem> findByQuotationId(String quotationId);
    void deleteByQuotationId(String quotationId);
}

