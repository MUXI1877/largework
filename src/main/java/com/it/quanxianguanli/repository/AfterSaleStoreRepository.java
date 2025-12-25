package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.AfterSaleStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterSaleStoreRepository extends JpaRepository<AfterSaleStore, String> {
}

