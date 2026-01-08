package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.OpportunityRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRegionRepository extends JpaRepository<OpportunityRegion, String> {
    List<OpportunityRegion> findByOpportunityId(String opportunityId);
    List<OpportunityRegion> findByRegionId(String regionId); // 根据片区ID查询所有关联的机会
    void deleteByOpportunityId(String opportunityId);
    void deleteByOpportunityIdAndRegionId(String opportunityId, String regionId); // 删除特定机会和片区的关联
}

