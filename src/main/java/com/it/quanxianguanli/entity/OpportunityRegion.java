package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunity_region")
public class OpportunityRegion extends BaseEntity {
    
    @Column(name = "opportunity_id", nullable = false, length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "region_id", nullable = false, length = 36)
    private String regionId; // 片区ID
    
    @Column(name = "region_name", length = 100)
    private String regionName; // 片区名称

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

