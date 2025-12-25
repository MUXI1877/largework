package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SpareParts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SparePartsRepository extends JpaRepository<SpareParts, String> {
}

