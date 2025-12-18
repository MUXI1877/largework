package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.PriceBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PriceBookRepository extends JpaRepository<PriceBook, String> {
    List<PriceBook> findByProductCode(String productCode);

    List<PriceBook> findByStatus(String status);

    List<PriceBook> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);

    List<PriceBook> findByExpireDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM PriceBook p WHERE p.productCode = :productCode AND p.effectiveDate <= :currentDate AND (p.expireDate IS NULL OR p.expireDate >= :currentDate)")
    PriceBook findCurrentPriceByProductCode(@Param("productCode") String productCode,
            @Param("currentDate") LocalDate currentDate);

    List<PriceBook> findByProductCodeOrderByEffectiveDateDesc(String productCode);
}