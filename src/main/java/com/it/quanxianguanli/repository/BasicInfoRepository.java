package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.BasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasicInfoRepository extends JpaRepository<BasicInfo, String> {
    List<BasicInfo> findByCategory(String category);

    Optional<BasicInfo> findByCode(String code);

    List<BasicInfo> findByParent_Id(String parentId);

    List<BasicInfo> findByCategoryAndIsEnabledTrue(String category);
}