package org.example.permissionsystembackend.repository;

import org.example.permissionsystembackend.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 模块Repository
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {

    List<Module> findByParentIdOrderBySortOrder(String parentId);

    List<Module> findByParentIdIsNullOrderBySortOrder();

    List<Module> findAllByOrderByLevelAscSortOrderAsc();
}
