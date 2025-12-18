package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<Receivable, String> {
    List<Receivable> findByOrder(SalesOrder order);
    List<Receivable> findByStatus(String status);
    List<Receivable> findByDueDateBefore(LocalDate date);
    List<Receivable> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
}