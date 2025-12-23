package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ProjectOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectOpportunityRepository extends JpaRepository<ProjectOpportunity, String>, JpaSpecificationExecutor<ProjectOpportunity> {
    List<ProjectOpportunity> findByCustomerId(String customerId);
    Optional<ProjectOpportunity> findByOpportunityCode(String opportunityCode);
}

