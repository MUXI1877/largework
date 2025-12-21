package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesPerson;
import com.it.quanxianguanli.service.SalesPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-person")
public class SalesPersonController {

    @Autowired
    private SalesPersonService salesPersonService;

    @GetMapping("/list")
    public Result<List<SalesPerson>> list(@RequestParam(required = false) String regionId) {
        if (regionId != null && !regionId.isEmpty()) {
            return Result.success(salesPersonService.findByRegionId(regionId));
        }
        return Result.success(salesPersonService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SalesPerson> getById(@PathVariable String id) {
        SalesPerson salesPerson = salesPersonService.findById(id);
        if (salesPerson == null) {
            return Result.error("营销人员不存在");
        }
        return Result.success(salesPerson);
    }

    @PostMapping("/save")
    public Result<SalesPerson> save(@RequestBody SalesPerson salesPerson) {
        return Result.success(salesPersonService.save(salesPerson));
    }

    @PutMapping("/update")
    public Result<SalesPerson> update(@RequestBody SalesPerson salesPerson) {
        if (salesPerson.getId() == null) {
            return Result.error("ID不能为空");
        }
        return Result.success(salesPersonService.save(salesPerson));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        salesPersonService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

