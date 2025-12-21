package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ProjectOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectOpportunityRepository extends JpaRepository<ProjectOpportunity, String> {
    List<ProjectOpportunity> findByCustomerId(String customerId);
}

