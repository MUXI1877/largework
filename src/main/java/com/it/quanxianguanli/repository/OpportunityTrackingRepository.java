package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.OpportunityTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityTrackingRepository extends JpaRepository<OpportunityTracking, String> {
    List<OpportunityTracking> findByOpportunityId(String opportunityId);
    List<OpportunityTracking> findByOpportunityIdOrderByCreateTimeDesc(String opportunityId);
}

