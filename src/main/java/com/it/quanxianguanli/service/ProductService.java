package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.Product;
import com.it.quanxianguanli.entity.ProductAttachment;
import com.it.quanxianguanli.repository.ProductRepository;
import com.it.quanxianguanli.repository.ProductAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

