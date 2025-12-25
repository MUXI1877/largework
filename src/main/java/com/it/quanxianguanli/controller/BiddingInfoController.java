package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.BiddingInfo;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.BiddingInfoService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/bidding-info")
public class BiddingInfoController {

    @Autowired
    private BiddingInfoService biddingInfoService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_BIDDING_INFO = "m018";

    private boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    private boolean hasPermission(HttpServletRequest request, String moduleId,
                                  Predicate<SysPermission> predicate) {
        if (isAdmin(request)) {
            return true;
        }
        Object roleId = request.getAttribute("roleId");
        if (roleId == null) {
            return false;
        }
        return permissionService.findByRoleIdAndModuleId(roleId.toString(), moduleId)
                .map(predicate::test)
                .orElse(false);
    }

    private <T> Result<T> forbidden() {
        return Result.error(403, "无权限操作");
    }

    @GetMapping("/list")
    public Result<List<BiddingInfo>> list(
            @RequestParam(required = false) String biddingName,
            @RequestParam(required = false) String biddingType,
            @RequestParam(required = false) String biddingStatus,
            @RequestParam(required = false) String customerName,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        List<BiddingInfo> biddings = biddingInfoService.findByConditions(
                biddingName, biddingType, biddingStatus, customerName);
        return Result.success(biddings);
    }

    @GetMapping("/{id}")
    public Result<BiddingInfo> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return biddingInfoService.findById(id)
                .map(Result::success)
                .orElse(Result.error("投标信息不存在"));
    }

    @PostMapping("/save")
    public Result<BiddingInfo> save(@RequestBody BiddingInfo biddingInfo, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            BiddingInfo saved = biddingInfoService.save(biddingInfo);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            biddingInfoService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/summary")
    public Result<Void> submitSummary(@PathVariable String id, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            String biddingResult = requestBody.get("biddingResult");
            String biddingSummary = requestBody.get("biddingSummary");
            String attachment = requestBody.get("attachment");
            biddingInfoService.submitSummary(id, biddingResult, biddingSummary, attachment);
            return Result.success("投标总结提交成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String biddingName,
            @RequestParam(required = false) String biddingType,
            @RequestParam(required = false) String biddingStatus,
            @RequestParam(required = false) String customerName,
            HttpServletResponse response,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_BIDDING_INFO, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<BiddingInfo> biddings = biddingInfoService.findByConditions(
                    biddingName, biddingType, biddingStatus, customerName);
            
            // 构建表头（列表展示字段：投标编号、投标名称、投标类型、投标客户、机会名称、机会编号、项目名称、技术方案、报价单、投标状态）
            List<String> headers = new ArrayList<>();
            headers.add("投标编号");
            headers.add("投标名称");
            headers.add("投标类型");
            headers.add("投标客户");
            headers.add("机会名称");
            headers.add("机会编号");
            headers.add("项目名称");
            headers.add("技术方案");
            headers.add("报价单");
            headers.add("投标状态");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (BiddingInfo bidding : biddings) {
                List<Object> row = new ArrayList<>();
                row.add(bidding.getBiddingCode());
                row.add(bidding.getBiddingName());
                row.add(bidding.getBiddingType());
                row.add(bidding.getCustomerName());
                row.add(bidding.getOpportunityName());
                row.add(bidding.getOpportunityCode());
                row.add(bidding.getProjectName());
                row.add(bidding.getTechnicalSolution() != null ? "已上传" : "未上传");
                row.add(bidding.getQuotationFile() != null ? "已上传" : "未上传");
                row.add(bidding.getBiddingStatus());
                data.add(row);
            }
            
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "投标信息列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}

