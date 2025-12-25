package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Bidding;
import com.it.quanxianguanli.entity.BiddingItem;
import com.it.quanxianguanli.repository.BiddingRepository;
import com.it.quanxianguanli.repository.BiddingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BiddingService {

  @Autowired
  private BiddingRepository biddingRepository;

  @Autowired
  private BiddingItemRepository biddingItemRepository;

  public List<Bidding> findAll() {
    return biddingRepository.findAll();
  }

  public Bidding findById(String id) {
    return biddingRepository.findById(id).orElse(null);
  }

  public List<BiddingItem> findItemsByBiddingId(String biddingId) {
    return biddingItemRepository.findByBiddingId(biddingId);
  }

  @Transactional
  public Bidding save(Bidding bidding) {
    return biddingRepository.save(bidding);
  }

  @Transactional
  public BiddingItem saveItem(BiddingItem item) {
    return biddingItemRepository.save(item);
  }

  @Transactional
  public void deleteById(String id) {
    biddingItemRepository.findByBiddingId(id).forEach(item -> biddingItemRepository.deleteById(item.getId()));
    biddingRepository.deleteById(id);
  }
}

