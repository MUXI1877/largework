package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.Product;
import com.it.quanxianguanli.entity.ProductAttachment;
import com.it.quanxianguanli.service.ProductService;
import com.it.quanxianguanli.util.ExcelUtil;
import com.it.quanxianguanli.util.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> list(@RequestParam(required = false) String productType) {
        if (productType != null && !productType.isEmpty()) {
            return Result.success(productService.findByType(productType));
        }
        return Result.success(productService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable String id) {
        return productService.findById(id)
                .map(Result::success)
                .orElse(Result.error("产品不存在"));
    }

    @PostMapping("/save")
    public Result<Product> save(@RequestBody Product product) {
        try {
            Product saved = productService.save(product);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            productService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/import")
    public Result<List<Product>> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productType") String productType) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 读取Excel文件
            List<List<String>> excelData = ExcelUtil.readExcel(file.getInputStream());
            if (excelData.isEmpty() || excelData.size() < 2) {
                return Result.error("Excel文件格式错误，至少需要表头和数据行");
            }
            
            // 解析数据（第一行为表头，从第二行开始为数据）
            List<Product> products = new ArrayList<>();
            for (int i = 1; i < excelData.size(); i++) {
                List<String> row = excelData.get(i);
                if (row.isEmpty() || row.get(0) == null || row.get(0).trim().isEmpty()) {
                    continue; // 跳过空行
                }
                
                Product product = new Product();
                product.setProductType(productType);
                
                // 根据列顺序解析：名称、型号、参数、价格、重量、尺寸、交货周期、备注
                if (row.size() > 0) product.setName(row.get(0));
                if (row.size() > 1) product.setModel(row.get(1));
                if (row.size() > 2) product.setParameters(row.get(2));
                if (row.size() > 3 && !row.get(3).isEmpty()) {
                    try {
                        product.setPrice(new BigDecimal(row.get(3)));
                    } catch (Exception e) {
                        // 忽略价格解析错误
                    }
                }
                if (row.size() > 4) product.setWeight(row.get(4));
                if (row.size() > 5) product.setDimensions(row.get(5));
                if (row.size() > 6) product.setDeliveryCycle(row.get(6));
                if (row.size() > 7) product.setRemarks(row.get(7));
                
                products.add(product);
            }
            
            if (products.isEmpty()) {
                return Result.error("Excel文件中没有有效数据");
            }
            
            // 批量保存
            List<Product> saved = productService.batchImport(products);
            return Result.success("成功导入 " + saved.size() + " 条数据", saved);
        } catch (Exception e) {
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(@RequestParam(required = false) String productType, HttpServletResponse response) {
        try {
            // 获取数据
            List<Product> products;
            if (productType != null && !productType.isEmpty()) {
                products = productService.findByType(productType);
            } else {
                products = productService.findAll();
            }
            
            // 构建表头
            List<String> headers = new ArrayList<>();
            headers.add("名称");
            headers.add("型号");
            headers.add("参数");
            headers.add("价格");
            headers.add("重量");
            headers.add("尺寸");
            headers.add("交货周期");
            headers.add("产品类型");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (Product product : products) {
                List<Object> row = new ArrayList<>();
                row.add(product.getName());
                row.add(product.getModel());
                row.add(product.getParameters());
                row.add(product.getPrice());
                row.add(product.getWeight());
                row.add(product.getDimensions());
                row.add(product.getDeliveryCycle());
                row.add(product.getProductType());
                row.add(product.getRemarks());
                data.add(row);
            }
            
            // 创建Excel
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            
            // 导出
            String filename = "产品列表_" + (productType != null ? productType : "全部") + ".xlsx";
            ExcelUtil.exportToResponse(response, workbook, filename);
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }

    // 附件管理
    @GetMapping("/{productId}/attachments")
    public Result<List<ProductAttachment>> getAttachments(@PathVariable String productId) {
        return Result.success(productService.getAttachments(productId));
    }

    @PostMapping("/attachment/save")
    public Result<ProductAttachment> saveAttachment(@RequestBody ProductAttachment attachment) {
        try {
            ProductAttachment saved = productService.saveAttachment(attachment);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/attachment/{id}")
    public Result<Void> deleteAttachment(@PathVariable String id) {
        try {
            productService.deleteAttachment(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/attachment/upload")
    public Result<ProductAttachment> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productId") String productId) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }
            
            // 保存文件
            String filePath = FileUtil.saveFile(file);
            String originalFilename = file.getOriginalFilename();
            
            // 创建附件记录
            ProductAttachment attachment = new ProductAttachment();
            attachment.setProductId(productId);
            attachment.setFileName(originalFilename != null ? originalFilename : "unknown");
            attachment.setFilePath(filePath);
            attachment.setFileSize(file.getSize());
            attachment.setFileType(FileUtil.getFileType(originalFilename != null ? originalFilename : ""));
            
            ProductAttachment saved = productService.saveAttachment(attachment);
            return Result.success("上传成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

