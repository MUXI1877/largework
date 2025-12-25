package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.PersonTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTransferRepository extends JpaRepository<PersonTransfer, String> {
}

