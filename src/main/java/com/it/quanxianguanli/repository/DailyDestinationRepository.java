package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.DailyDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyDestinationRepository extends JpaRepository<DailyDestination, String> {
    
    List<DailyDestination> findByUserIdOrderByDestinationTimeDesc(String userId);
    
    List<DailyDestination> findAllByOrderByDestinationTimeDesc();
    
    List<DailyDestination> findByDestinationTimeBetween(LocalDateTime start, LocalDateTime end);
}

