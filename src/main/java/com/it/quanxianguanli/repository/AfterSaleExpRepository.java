package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AfterSaleExp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AfterSaleExpRepository extends JpaRepository<AfterSaleExp, String> {
  List<AfterSaleExp> findByDeviceModel(String deviceModel);
  List<AfterSaleExp> findByProblemContaining(String problem);
}

