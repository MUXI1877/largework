package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.entity.PriceBookLog;
import com.it.quanxianguanli.entity.Product;
import com.it.quanxianguanli.repository.PriceBookLogRepository;
import com.it.quanxianguanli.repository.PriceBookRepository;
import com.it.quanxianguanli.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PriceBookService {
    
    @Autowired
    private PriceBookRepository priceBookRepository;
    
    @Autowired
    private PriceBookLogRepository priceBookLogRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * 分页查询价格本
     */
    public Page<PriceBook> findPriceBooks(String productType, String department, 
                                         String productName, String model, Pageable pageable) {
        Specification<PriceBook> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (productType != null && !productType.isEmpty()) {
                predicates.add(cb.equal(root.get("productType"), productType));
            }
            
            if (department != null && !department.isEmpty()) {
                predicates.add(cb.equal(root.get("department"), department));
            }
            
            if (productName != null && !productName.isEmpty()) {
                predicates.add(cb.like(root.get("productName"), "%" + productName + "%"));
            }
            
            if (model != null && !model.isEmpty()) {
                predicates.add(cb.like(root.get("model"), "%" + model + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return priceBookRepository.findAll(spec, pageable);
    }
    
    /**
     * 根据ID查询价格本
     */
    public Optional<PriceBook> findById(String id) {
        return priceBookRepository.findById(id);
    }
    
    /**
     * 根据产品ID和部门查询价格本
     */
    public Optional<PriceBook> findByProductIdAndDepartment(String productId, String department) {
        return priceBookRepository.findByProductIdAndDepartment(productId, department);
    }
    
    /**
     * 根据产品ID查询所有部门的价格本
     */
    public List<PriceBook> findByProductId(String productId) {
        return priceBookRepository.findByProductId(productId);
    }
    
    /**
     * 根据部门查询价格本
     */
    public List<PriceBook> findByDepartment(String department) {
        return priceBookRepository.findByDepartment(department);
    }
    
    /**
     * 新增价格本
     * 自动关联产品信息
     */
    @Transactional
    public PriceBook createPriceBook(PriceBook priceBook, String operatorId, String operatorName) {
        // 验证产品是否存在
        Optional<Product> productOpt = productRepository.findById(priceBook.getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("产品不存在");
        }
        
        Product product = productOpt.get();
        
        // 检查是否已存在相同产品ID和部门的价格本
        if (priceBookRepository.existsByProductIdAndDepartment(priceBook.getProductId(), priceBook.getDepartment())) {
            throw new RuntimeException("该产品在该部门的价格本已存在");
        }
        
        // 同步产品信息
        priceBook.setProductName(product.getName());
        priceBook.setProductType(product.getProductType());
        priceBook.setModel(product.getModel());
        priceBook.setParameters(product.getParameters());
        
        // 保存价格本
        PriceBook saved = priceBookRepository.save(priceBook);
        
        // 记录操作日志
        PriceBookLog log = new PriceBookLog();
        log.setPriceBookId(saved.getId());
        log.setProductId(saved.getProductId());
        log.setProductName(saved.getProductName());
        log.setDepartment(saved.getDepartment());
        log.setOldPrice(null);
        log.setNewPrice(saved.getUnitPrice());
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperationType("CREATE");
        log.setRemarks("新增价格本");
        priceBookLogRepository.save(log);
        
        return saved;
    }
    
    /**
     * 更新价格本
     * 记录价格变更日志
     */
    @Transactional
    public PriceBook updatePriceBook(String id, PriceBook priceBook, String operatorId, String operatorName) {
        Optional<PriceBook> existingOpt = priceBookRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("价格本不存在");
        }
        
        PriceBook existing = existingOpt.get();
        BigDecimal oldPrice = existing.getUnitPrice();
        
        // 更新价格本信息
        existing.setUnitPrice(priceBook.getUnitPrice());
        existing.setRemarks(priceBook.getRemarks());
        
        // 如果产品ID改变，需要同步产品信息
        if (!existing.getProductId().equals(priceBook.getProductId())) {
            Optional<Product> productOpt = productRepository.findById(priceBook.getProductId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("产品不存在");
            }
            
            Product product = productOpt.get();
            existing.setProductId(product.getId());
            existing.setProductName(product.getName());
            existing.setProductType(product.getProductType());
            existing.setModel(product.getModel());
            existing.setParameters(product.getParameters());
        }
        
        PriceBook saved = priceBookRepository.save(existing);
        
        // 如果价格发生变化，记录日志
        if (oldPrice != null && !oldPrice.equals(saved.getUnitPrice())) {
            PriceBookLog log = new PriceBookLog();
            log.setPriceBookId(saved.getId());
            log.setProductId(saved.getProductId());
            log.setProductName(saved.getProductName());
            log.setDepartment(saved.getDepartment());
            log.setOldPrice(oldPrice);
            log.setNewPrice(saved.getUnitPrice());
            log.setOperatorId(operatorId);
            log.setOperatorName(operatorName);
            log.setOperationType("UPDATE");
            log.setRemarks("修改价格：" + oldPrice + " -> " + saved.getUnitPrice());
            priceBookLogRepository.save(log);
        }
        
        return saved;
    }
    
    /**
     * 删除价格本
     */
    @Transactional
    public void deletePriceBook(String id, String operatorId, String operatorName) {
        Optional<PriceBook> existingOpt = priceBookRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("价格本不存在");
        }
        
        PriceBook existing = existingOpt.get();
        
        // 记录删除日志
        PriceBookLog log = new PriceBookLog();
        log.setPriceBookId(existing.getId());
        log.setProductId(existing.getProductId());
        log.setProductName(existing.getProductName());
        log.setDepartment(existing.getDepartment());
        log.setOldPrice(existing.getUnitPrice());
        log.setNewPrice(null);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperationType("DELETE");
        log.setRemarks("删除价格本");
        priceBookLogRepository.save(log);
        
        priceBookRepository.deleteById(id);
    }
    
    /**
     * 查询价格修改日志
     */
    public List<PriceBookLog> findPriceBookLogs(String priceBookId) {
        return priceBookLogRepository.findByPriceBookIdOrderByCreateTimeDesc(priceBookId);
    }
    
    /**
     * 根据产品ID和部门获取价格（用于销售报价管理模块调用）
     */
    public BigDecimal getPriceByProductIdAndDepartment(String productId, String department) {
        Optional<PriceBook> priceBook = priceBookRepository.findByProductIdAndDepartment(productId, department);
        return priceBook.map(PriceBook::getUnitPrice).orElse(null);
    }
    
    /**
     * 导出所有价格本数据
     */
    public List<PriceBook> exportAllPriceBooks() {
        return priceBookRepository.findAll();
    }
    
    /**
     * 导出筛选后的价格本数据
     */
    public List<PriceBook> exportFilteredPriceBooks(String productType, String department, 
                                                   String productName, String model) {
        Specification<PriceBook> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (productType != null && !productType.isEmpty()) {
                predicates.add(cb.equal(root.get("productType"), productType));
            }
            
            if (department != null && !department.isEmpty()) {
                predicates.add(cb.equal(root.get("department"), department));
            }
            
            if (productName != null && !productName.isEmpty()) {
                predicates.add(cb.like(root.get("productName"), "%" + productName + "%"));
            }
            
            if (model != null && !model.isEmpty()) {
                predicates.add(cb.like(root.get("model"), "%" + model + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return priceBookRepository.findAll(spec);
    }
}

