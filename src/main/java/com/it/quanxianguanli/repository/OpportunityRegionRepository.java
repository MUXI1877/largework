package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.OpportunityRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRegionRepository extends JpaRepository<OpportunityRegion, String> {
    List<OpportunityRegion> findByOpportunityId(String opportunityId);
    void deleteByOpportunityId(String opportunityId);
}

