package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.DailyDestination;
import com.it.quanxianguanli.repository.DailyDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DailyDestinationService {

    @Autowired
    private DailyDestinationRepository dailyDestinationRepository;

    public List<DailyDestination> findAll() {
        return dailyDestinationRepository.findAllByOrderByDestinationTimeDesc();
    }

    public List<DailyDestination> findByUserId(String userId) {
        return dailyDestinationRepository.findByUserIdOrderByDestinationTimeDesc(userId);
    }

    public DailyDestination findById(String id) {
        return dailyDestinationRepository.findById(id).orElse(null);
    }

    @Transactional
    public DailyDestination save(DailyDestination dailyDestination) {
        return dailyDestinationRepository.save(dailyDestination);
    }

    @Transactional
    public void deleteById(String id) {
        dailyDestinationRepository.deleteById(id);
    }
}

