package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AfterSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSaleRepository extends JpaRepository<AfterSale, String> {
  List<AfterSale> findByDeviceSn(String deviceSn);
  List<AfterSale> findByStatus(String status);
  List<AfterSale> findByHandlerId(String handlerId);
}

