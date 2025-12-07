package org.example.permissionsystembackend.repository;

import org.example.permissionsystembackend.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 选项Repository
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, String> {

    List<Option> findByGroupNameOrderBySortOrder(String groupName);

    List<Option> findAllByOrderByGroupNameAscSortOrderAsc();
}

