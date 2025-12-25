package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, String> {
    
    List<WeeklyReport> findByUserIdOrderByReportTimeDesc(String userId);
    
    List<WeeklyReport> findAllByOrderByReportTimeDesc();
    
    List<WeeklyReport> findByStatusOrderByReportTimeDesc(String status);
}

