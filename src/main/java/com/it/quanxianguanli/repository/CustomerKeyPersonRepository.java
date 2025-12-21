package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.CustomerKeyPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerKeyPersonRepository extends JpaRepository<CustomerKeyPerson, String> {
    List<CustomerKeyPerson> findByCustomerId(String customerId);
    void deleteByCustomerId(String customerId);
}

