package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysOptionRepository extends JpaRepository<SysOption, String> {
    List<SysOption> findByGroupOrderBySortAsc(String group);
}
