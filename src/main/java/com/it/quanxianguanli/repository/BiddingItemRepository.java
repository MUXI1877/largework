package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.BiddingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiddingItemRepository extends JpaRepository<BiddingItem, String> {
  List<BiddingItem> findByBiddingId(String biddingId);
}

