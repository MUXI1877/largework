package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.PriceBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceBookRepository extends JpaRepository<PriceBook, String> {
}

