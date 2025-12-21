package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUnitName(String unitName);
    Optional<Customer> findByCustomerCode(String customerCode);
}

