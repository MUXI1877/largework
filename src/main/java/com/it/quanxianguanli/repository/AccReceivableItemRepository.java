package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AccReceivableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccReceivableItemRepository extends JpaRepository<AccReceivableItem, String> {
  List<AccReceivableItem> findByContractId(String contractId);
}

