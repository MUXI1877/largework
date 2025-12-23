package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.OpportunityEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityEmployeeRepository extends JpaRepository<OpportunityEmployee, String> {
    List<OpportunityEmployee> findByOpportunityId(String opportunityId);
    void deleteByOpportunityId(String opportunityId);
}

