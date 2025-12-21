package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_attachment")
public class ProductAttachment extends BaseEntity {
    
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId; // 产品ID
    
    @Column(name = "file_name", nullable = false, length = 200)
    private String fileName; // 文件名
    
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath; // 文件路径
    
    @Column(name = "file_size")
    private Long fileSize; // 文件大小（字节）
    
    @Column(name = "file_type", length = 50)
    private String fileType; // 文件类型

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}

