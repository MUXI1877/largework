package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.BiddingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiddingInfoRepository extends JpaRepository<BiddingInfo, String>, JpaSpecificationExecutor<BiddingInfo> {
    Optional<BiddingInfo> findByBiddingCode(String biddingCode);
    List<BiddingInfo> findByOpportunityId(String opportunityId);
}

