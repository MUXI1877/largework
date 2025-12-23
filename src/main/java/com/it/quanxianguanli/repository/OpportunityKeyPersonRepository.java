package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.OpportunityKeyPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityKeyPersonRepository extends JpaRepository<OpportunityKeyPerson, String> {
    List<OpportunityKeyPerson> findByOpportunityId(String opportunityId);
    void deleteByOpportunityId(String opportunityId);
}

