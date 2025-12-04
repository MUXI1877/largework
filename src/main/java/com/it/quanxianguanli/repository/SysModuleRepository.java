package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, String> {
    List<SysModule> findByParentIdOrderBySortAsc(String parentId);
    List<SysModule> findByParentIdIsNullOrderBySortAsc();
}

