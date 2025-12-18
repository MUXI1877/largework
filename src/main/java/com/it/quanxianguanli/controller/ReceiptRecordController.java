package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.ReceiptRecord;
import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.service.ReceiptRecordService;
import com.it.quanxianguanli.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/receipt-record")
public class ReceiptRecordController {

    @Autowired
    private ReceiptRecordService receiptRecordService;

    @Autowired
    private ReceivableService receivableService;

    @GetMapping("/list")
    public Result<List<ReceiptRecord>> list() {
        return Result.success(receiptRecordService.findAll());
    }

    @GetMapping("/receivable/{receivableId}")
    public Result<List<ReceiptRecord>> listByReceivableId(@PathVariable String receivableId) {
        return receivableService.findById(receivableId)
                .map(receivable -> Result.success(receiptRecordService.findByReceivable(receivable)))
                .orElse(Result.error("应收账不存在"));
    }

    @GetMapping("/date-range")
    public Result<List<ReceiptRecord>> listByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(receiptRecordService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/method/{receiptMethod}")
    public Result<List<ReceiptRecord>> listByReceiptMethod(@PathVariable String receiptMethod) {
        return Result.success(receiptRecordService.findByReceiptMethod(receiptMethod));
    }

    @GetMapping("/{id}")
    public Result<ReceiptRecord> getById(@PathVariable String id) {
        return receiptRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error("收款记录不存在"));
    }

    @PostMapping("/save")
    public Result<ReceiptRecord> save(@RequestBody ReceiptRecord receiptRecord) {
        try {
            // 确保关联的应收账存在
            Receivable receivable = receiptRecord.getReceivable();
            if (receivable != null && receivable.getId() != null) {
                Receivable existing = receivableService.findById(receivable.getId())
                        .orElseThrow(() -> new RuntimeException("应收账不存在"));
                receiptRecord.setReceivable(existing);
            }
            return Result.success(receiptRecordService.save(receiptRecord));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/save-all")
    public Result<List<ReceiptRecord>> saveAll(@RequestBody List<ReceiptRecord> records) {
        try {
            return Result.success(receiptRecordService.saveAll(records));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<ReceiptRecord> update(@RequestBody ReceiptRecord receiptRecord) {
        try {
            return receiptRecordService.findById(receiptRecord.getId())
                    .map(record -> Result.success(receiptRecordService.update(receiptRecord)))
                    .orElse(Result.error("收款记录不存在"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            receiptRecordService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}