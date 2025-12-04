package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, String> {
    List<SysModule> findByParentIdOrderBySortAsc(String parentId);
    List<SysModule> findByParentIdIsNullOrderBySortAsc();
    SysModule findByEnName(String enName);

    @Query("SELECT COUNT(p) FROM SysPermission p WHERE p.moduleId = :moduleId")
    Long countRoleReferences(@Param("moduleId") String moduleId);
}