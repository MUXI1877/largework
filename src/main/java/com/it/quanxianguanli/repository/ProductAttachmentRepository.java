package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.ProductAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttachmentRepository extends JpaRepository<ProductAttachment, String> {
    List<ProductAttachment> findByProductId(String productId);
    void deleteByProductId(String productId);
}

