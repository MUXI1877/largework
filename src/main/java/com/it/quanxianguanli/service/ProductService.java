package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Product;
import com.it.quanxianguanli.entity.ProductAttachment;
import com.it.quanxianguanli.repository.ProductRepository;
import com.it.quanxianguanli.repository.ProductAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductAttachmentRepository attachmentRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByType(String productType) {
        return productRepository.findByProductType(productType);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public void delete(String id) {
        // 删除关联附件（包括文件）
        List<ProductAttachment> attachments = attachmentRepository.findByProductId(id);
        for (ProductAttachment attachment : attachments) {
            com.it.quanxianguanli.util.FileUtil.deleteFile(attachment.getFilePath());
        }
        attachmentRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    // 附件管理
    public List<ProductAttachment> getAttachments(String productId) {
        return attachmentRepository.findByProductId(productId);
    }

    @Transactional
    public ProductAttachment saveAttachment(ProductAttachment attachment) {
        return attachmentRepository.save(attachment);
    }

    @Transactional
    public void deleteAttachment(String id) {
        Optional<ProductAttachment> attachmentOpt = attachmentRepository.findById(id);
        if (attachmentOpt.isPresent()) {
            ProductAttachment attachment = attachmentOpt.get();
            // 删除文件
            com.it.quanxianguanli.util.FileUtil.deleteFile(attachment.getFilePath());
            attachmentRepository.deleteById(id);
        }
    }

    @Transactional
    public List<Product> batchImport(List<Product> products) {
        return productRepository.saveAll(products);
    }

    // 库存查询（排除已标记降库的产品）
    public List<Product> queryInventory(String drawingNumber, String name, Boolean isStagnant) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 排除已标记降库的产品
            predicates.add(cb.or(
                cb.isNull(root.get("isReducedStock")),
                cb.equal(root.get("isReducedStock"), false)
            ));
            
            if (drawingNumber != null && !drawingNumber.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("drawingNumber")), "%" + drawingNumber.toLowerCase() + "%"));
            }
            
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            
            if (isStagnant != null) {
                predicates.add(cb.equal(root.get("isStagnant"), isStagnant));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return productRepository.findAll(spec);
    }

    // 降库产品查询（支持多条件筛选，排除已标记降库的产品）
    public List<Product> queryReducedStockProducts(String caliber, String motorPower, String flow, 
                                                    String head, String filterMaterial, 
                                                    String inletPressure, String outletPressure) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 只查询呆滞产品
            predicates.add(cb.equal(root.get("isStagnant"), true));
            
            // 排除已标记降库的产品
            predicates.add(cb.or(
                cb.isNull(root.get("isReducedStock")),
                cb.equal(root.get("isReducedStock"), false)
            ));
            
            if (caliber != null && !caliber.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("caliber")), "%" + caliber.toLowerCase() + "%"));
            }
            
            if (motorPower != null && !motorPower.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("motorPower")), "%" + motorPower.toLowerCase() + "%"));
            }
            
            if (flow != null && !flow.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("flow")), "%" + flow.toLowerCase() + "%"));
            }
            
            if (head != null && !head.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("head")), "%" + head.toLowerCase() + "%"));
            }
            
            if (filterMaterial != null && !filterMaterial.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("filterMaterial")), "%" + filterMaterial.toLowerCase() + "%"));
            }
            
            if (inletPressure != null && !inletPressure.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("inletPressure")), "%" + inletPressure.toLowerCase() + "%"));
            }
            
            if (outletPressure != null && !outletPressure.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("outletPressure")), "%" + outletPressure.toLowerCase() + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return productRepository.findAll(spec);
    }

    // 标记降库产品
    @Transactional
    public void markReducedStock(String productId, String contractId) {
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isPresent()) {
            Product product = opt.get();
            product.setIsReducedStock(true);
            product.setContractId(contractId);
            productRepository.save(product);
        } else {
            throw new RuntimeException("产品不存在");
        }
    }
}

