package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.WeeklyReport;
import com.it.quanxianguanli.repository.WeeklyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WeeklyReportService {

    @Autowired
    private WeeklyReportRepository weeklyReportRepository;

    public List<WeeklyReport> findAll() {
        return weeklyReportRepository.findAllByOrderByReportTimeDesc();
    }

    public List<WeeklyReport> findByUserId(String userId) {
        return weeklyReportRepository.findByUserIdOrderByReportTimeDesc(userId);
    }

    public WeeklyReport findById(String id) {
        return weeklyReportRepository.findById(id).orElse(null);
    }

    @Transactional
    public WeeklyReport save(WeeklyReport weeklyReport) {
        return weeklyReportRepository.save(weeklyReport);
    }

    @Transactional
    public void deleteById(String id) {
        weeklyReportRepository.deleteById(id);
    }

    @Transactional
    public WeeklyReport submit(String id) {
        WeeklyReport report = findById(id);
        if (report != null) {
            report.setStatus("submitted");
            return weeklyReportRepository.save(report);
        }
        return null;
    }
}

