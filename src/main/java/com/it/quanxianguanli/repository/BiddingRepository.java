package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.Bidding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiddingRepository extends JpaRepository<Bidding, String> {
}

