package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectOpportunityService {

    @Autowired
    private ProjectOpportunityRepository projectOpportunityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OpportunityRegionRepository opportunityRegionRepository;

    @Autowired
    private OpportunityEmployeeRepository opportunityEmployeeRepository;

    @Autowired
    private OpportunityKeyPersonRepository opportunityKeyPersonRepository;

    @Autowired
    private OpportunityTrackingRepository opportunityTrackingRepository;

    @Autowired
    private SalesRegionRepository salesRegionRepository;

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    public List<ProjectOpportunity> findAll() {
        return projectOpportunityRepository.findAll();
    }

    public List<ProjectOpportunity> findByConditions(String opportunityName, LocalDate startDate, LocalDate endDate,
            String status, String source, String industry) {
        Specification<ProjectOpportunity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (opportunityName != null && !opportunityName.trim().isEmpty()) {
                predicates
                        .add(cb.like(cb.lower(root.get("opportunityName")), "%" + opportunityName.toLowerCase() + "%"));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("opportunityDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("opportunityDate"), endDate));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (source != null && !source.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("source"), source));
            }

            if (industry != null && !industry.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("industry"), industry));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return projectOpportunityRepository.findAll(spec);
    }

    /**
     * 根据用户角色和权限查询销售机会
     * 普通用户：只能看到自己创建的、未关闭、未提交的机会
     * 系统管理员：可以看到所有普通用户提交的机会
     * 超级管理员：可以看到所有机会
     */
    public List<ProjectOpportunity> findByUserRole(String userId, String roleId, String opportunityName,
            LocalDate startDate, LocalDate endDate,
            String status, String source, String industry) {
        Specification<ProjectOpportunity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 根据角色过滤数据
            if ("r003".equals(roleId)) {
                // 普通用户：只能看到自己创建的机会（包括已提交的，用于查看状态）
                // 不能看到系统管理员或超级管理员创建的机会
                if (userId != null && !userId.isEmpty()) {
                    // 严格限制：只能看到自己创建的机会
                    predicates.add(cb.equal(root.get("creatorId"), userId));
                } else {
                    // 如果没有userId，返回空结果
                    predicates.add(cb.equal(root.get("id"), ""));
                }
                // 普通用户可以看到自己创建的所有机会（包括已提交的），以便查看状态
                // 不限制isSubmitted状态，让用户能看到自己提交后的机会状态
                // 注意：普通用户绝对不能看到系统管理员或超级管理员创建的机会
            } else if ("r002".equals(roleId)) {
                // 系统管理员：可以看到
                // 1. 所有普通用户提交的机会（已提交且未上传）
                // 2. 自己创建的机会（不管是否提交）
                // 3. 其他系统管理员创建的机会（用于协作）
                // 先获取所有普通用户的ID列表
                List<SysUser> commonUsers = sysUserRepository.findByRoleId("r003");
                List<String> commonUserIds = commonUsers.stream()
                        .map(SysUser::getId)
                        .filter(id -> id != null && !id.isEmpty())
                        .toList();

                // 获取所有系统管理员的ID列表
                List<SysUser> adminUsers = sysUserRepository.findByRoleId("r002");
                List<String> adminUserIds = adminUsers.stream()
                        .map(SysUser::getId)
                        .filter(id -> id != null && !id.isEmpty())
                        .toList();

                // 系统管理员可以看到：
                // 1. 普通用户提交的机会（已提交且未上传）
                // 2. 自己创建的机会（不管是否提交）
                // 3. 其他系统管理员创建的机会（不管是否提交）
                List<Predicate> adminPredicates = new ArrayList<>();

                // 条件1：普通用户提交的机会（已提交且未上传）
                if (!commonUserIds.isEmpty()) {
                    adminPredicates.add(cb.and(
                            root.get("creatorId").in(commonUserIds),
                            cb.equal(root.get("isSubmitted"), true),
                            cb.or(
                                    cb.isNull(root.get("status")),
                                    cb.notEqual(root.get("status"), "已上传"))));
                }

                // 条件2：自己创建的机会（不管是否提交）
                if (userId != null && !userId.isEmpty()) {
                    adminPredicates.add(cb.equal(root.get("creatorId"), userId));
                }

                // 条件3：其他系统管理员创建的机会（不管是否提交，用于协作）
                if (!adminUserIds.isEmpty()) {
                    if (userId != null && !userId.isEmpty()) {
                        // 排除自己，只看其他系统管理员的
                        List<String> otherAdminIds = adminUserIds.stream()
                                .filter(id -> !id.equals(userId))
                                .toList();
                        if (!otherAdminIds.isEmpty()) {
                            adminPredicates.add(root.get("creatorId").in(otherAdminIds));
                        }
                    } else {
                        // 如果没有userId，看所有系统管理员的
                        adminPredicates.add(root.get("creatorId").in(adminUserIds));
                    }
                }

                if (!adminPredicates.isEmpty()) {
                    predicates.add(cb.or(adminPredicates.toArray(new Predicate[0])));
                } else {
                    // 如果没有条件，返回空结果
                    predicates.add(cb.equal(root.get("id"), ""));
                }
            } else if ("r001".equals(roleId)) {
                // 超级管理员：可以看到
                // 1. 所有普通用户提交的机会（已提交）
                // 2. 系统管理员上传的机会（已上传）
                // 3. 自己创建的机会（不管是否提交）
                // 先获取所有普通用户的ID列表
                List<SysUser> commonUsers = sysUserRepository.findByRoleId("r003");
                List<String> commonUserIds = commonUsers.stream()
                        .map(SysUser::getId)
                        .filter(id -> id != null && !id.isEmpty())
                        .toList();

                // 获取所有系统管理员的ID列表
                List<SysUser> adminUsers = sysUserRepository.findByRoleId("r002");
                List<String> adminUserIds = adminUsers.stream()
                        .map(SysUser::getId)
                        .filter(id -> id != null && !id.isEmpty())
                        .toList();

                List<Predicate> superAdminPredicates = new ArrayList<>();

                // 条件1：普通用户提交的机会（已提交）
                if (!commonUserIds.isEmpty()) {
                    superAdminPredicates.add(cb.and(
                            root.get("creatorId").in(commonUserIds),
                            cb.equal(root.get("isSubmitted"), true)));
                }

                // 条件2：系统管理员上传的机会（已上传）
                if (!adminUserIds.isEmpty()) {
                    superAdminPredicates.add(cb.and(
                            root.get("creatorId").in(adminUserIds),
                            cb.equal(root.get("status"), "已上传")));
                }

                // 条件3：自己创建的机会（不管是否提交）
                if (userId != null) {
                    superAdminPredicates.add(cb.equal(root.get("creatorId"), userId));
                }

                if (!superAdminPredicates.isEmpty()) {
                    predicates.add(cb.or(superAdminPredicates.toArray(new Predicate[0])));
                }
                // 如果没有条件，超级管理员可以看到所有机会（兼容旧逻辑）
            }

            // 通用查询条件
            if (opportunityName != null && !opportunityName.trim().isEmpty()) {
                predicates
                        .add(cb.like(cb.lower(root.get("opportunityName")), "%" + opportunityName.toLowerCase() + "%"));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("opportunityDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("opportunityDate"), endDate));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (source != null && !source.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("source"), source));
            }

            if (industry != null && !industry.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("industry"), industry));
            }

            // 过滤掉没有ID的记录
            predicates.add(cb.isNotNull(root.get("id")));
            predicates.add(cb.notEqual(root.get("id"), ""));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return projectOpportunityRepository.findAll(spec);
    }

    /**
     * 根据用户所属片区查询销售机会
     * 包括：1. 原始属于该片区的机会 2. 被传递到该片区的机会
     */
    public List<ProjectOpportunity> findByUserRegion(String userRegionId, String opportunityName,
            LocalDate startDate, LocalDate endDate,
            String status, String source, String industry) {
        // 1. 查询原始属于该片区的机会
        Specification<ProjectOpportunity> ownerSpec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("ownerRegionId"), userRegionId));

            if (opportunityName != null && !opportunityName.trim().isEmpty()) {
                predicates
                        .add(cb.like(cb.lower(root.get("opportunityName")), "%" + opportunityName.toLowerCase() + "%"));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("opportunityDate"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("opportunityDate"), endDate));
            }

            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (source != null && !source.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("source"), source));
            }

            if (industry != null && !industry.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("industry"), industry));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<ProjectOpportunity> ownerOpportunities = projectOpportunityRepository.findAll(ownerSpec);

        // 2. 查询被传递到该片区的机会
        List<OpportunityRegion> transferredRegions = opportunityRegionRepository.findByRegionId(userRegionId);
        List<String> transferredOpportunityIds = transferredRegions.stream()
                .map(OpportunityRegion::getOpportunityId)
                .distinct()
                .toList();

        List<ProjectOpportunity> transferredOpportunities = new ArrayList<>();
        if (!transferredOpportunityIds.isEmpty()) {
            Specification<ProjectOpportunity> transferredSpec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(root.get("id").in(transferredOpportunityIds));

                if (opportunityName != null && !opportunityName.trim().isEmpty()) {
                    predicates.add(
                            cb.like(cb.lower(root.get("opportunityName")), "%" + opportunityName.toLowerCase() + "%"));
                }

                if (startDate != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("opportunityDate"), startDate));
                }

                if (endDate != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("opportunityDate"), endDate));
                }

                if (status != null && !status.trim().isEmpty()) {
                    predicates.add(cb.equal(root.get("status"), status));
                }

                if (source != null && !source.trim().isEmpty()) {
                    predicates.add(cb.equal(root.get("source"), source));
                }

                if (industry != null && !industry.trim().isEmpty()) {
                    predicates.add(cb.equal(root.get("industry"), industry));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            };

            transferredOpportunities = projectOpportunityRepository.findAll(transferredSpec);
        }

        // 3. 合并结果并去重
        List<ProjectOpportunity> allOpportunities = new ArrayList<>(ownerOpportunities);
        for (ProjectOpportunity opp : transferredOpportunities) {
            if (allOpportunities.stream().noneMatch(o -> o.getId().equals(opp.getId()))) {
                allOpportunities.add(opp);
            }
        }

        return allOpportunities;
    }

    public Optional<ProjectOpportunity> findById(String id) {
        return projectOpportunityRepository.findById(id);
    }

    @Transactional
    public ProjectOpportunity save(ProjectOpportunity opportunity, String creatorId) {
        // 生成机会编号
        if (opportunity.getOpportunityCode() == null || opportunity.getOpportunityCode().isEmpty()) {
            opportunity.setOpportunityCode("OPP" + System.currentTimeMillis());
        }

        // 设置创建人ID（新增时设置，修改时不改变）
        if (opportunity.getId() == null || opportunity.getId().isEmpty()) {
            opportunity.setCreatorId(creatorId);
        }

        // 如果未提交，状态设置为"草稿"
        if (!opportunity.getIsSubmitted() && (opportunity.getStatus() == null || opportunity.getStatus().isEmpty())) {
            opportunity.setStatus("草稿");
        }

        // 如果选择了客户，自动关联客户信息
        if (opportunity.getCustomerId() != null && !opportunity.getCustomerId().isEmpty()) {
            Optional<Customer> customerOpt = customerRepository.findById(opportunity.getCustomerId());
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                opportunity.setCustomerName(customer.getUnitName());
                opportunity.setIndustry(customer.getIndustry());
            }
        }

        return projectOpportunityRepository.save(opportunity);
    }

    @Transactional
    public void delete(String id, String userId, String roleId) {
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(id);
        if (!opt.isPresent()) {
            throw new RuntimeException("销售机会不存在");
        }

        ProjectOpportunity opportunity = opt.get();

        // 检查是否已提交
        if (opportunity.getIsSubmitted()) {
            throw new RuntimeException("已提交的销售机会不可删除");
        }

        // 普通用户只能删除自己创建的机会
        if ("r003".equals(roleId) && !userId.equals(opportunity.getCreatorId())) {
            throw new RuntimeException("只能删除自己创建的销售机会");
        }

        // 删除关联数据
        List<OpportunityTracking> trackings = opportunityTrackingRepository.findByOpportunityId(id);
        for (OpportunityTracking tracking : trackings) {
            opportunityTrackingRepository.deleteById(tracking.getId());
        }
        opportunityRegionRepository.deleteByOpportunityId(id);
        opportunityEmployeeRepository.deleteByOpportunityId(id);
        opportunityKeyPersonRepository.deleteByOpportunityId(id);

        projectOpportunityRepository.deleteById(id);
    }

    @Transactional
    public void submit(String id, String userId, String roleId) {
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(id);
        if (!opt.isPresent()) {
            throw new RuntimeException("销售机会不存在");
        }

        ProjectOpportunity opportunity = opt.get();

        // 普通用户只能提交自己创建的机会
        if ("r003".equals(roleId) && !userId.equals(opportunity.getCreatorId())) {
            throw new RuntimeException("只能提交自己创建的销售机会");
        }

        opportunity.setIsSubmitted(true);
        opportunity.setStatus("已提交");
        projectOpportunityRepository.save(opportunity);
    }

    /**
     * 系统管理员向超级管理员上传机会
     * 将已提交的机会标记为已上传给超级管理员
     */
    @Transactional
    public void uploadToSuperAdmin(String id, String roleId) {
        // 只有系统管理员可以上传
        if (!"r002".equals(roleId)) {
            throw new RuntimeException("只有系统管理员可以向超级管理员上传机会");
        }

        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(id);
        if (!opt.isPresent()) {
            throw new RuntimeException("销售机会不存在");
        }

        ProjectOpportunity opportunity = opt.get();

        // 只能上传已提交且未关闭的机会
        if (!opportunity.getIsSubmitted()) {
            throw new RuntimeException("只能上传已提交的销售机会");
        }

        if ("已关闭".equals(opportunity.getStatus())) {
            throw new RuntimeException("已关闭的销售机会不能上传");
        }

        // 标记为已上传（可以通过状态或备注来标识）
        opportunity.setStatus("已上传");
        opportunity.setRemarks((opportunity.getRemarks() != null ? opportunity.getRemarks() + "\n" : "") +
                "已上传给超级管理员");
        projectOpportunityRepository.save(opportunity);
    }

    @Transactional
    public void transferRegions(String opportunityId, List<String> regionIds) {
        // 获取机会信息，确保原始片区被保留
        Optional<ProjectOpportunity> oppOpt = projectOpportunityRepository.findById(opportunityId);
        if (!oppOpt.isPresent()) {
            throw new RuntimeException("销售机会不存在");
        }

        ProjectOpportunity opportunity = oppOpt.get();
        String ownerRegionId = opportunity.getOwnerRegionId();

        // 获取当前已关联的片区（不包括原始片区）
        List<OpportunityRegion> existingRegions = opportunityRegionRepository.findByOpportunityId(opportunityId);
        List<String> existingRegionIds = existingRegions.stream()
                .map(OpportunityRegion::getRegionId)
                .filter(id -> !id.equals(ownerRegionId)) // 排除原始片区
                .toList();

        // 删除不再传递的片区关联（保留原始片区）
        for (String existingId : existingRegionIds) {
            if (!regionIds.contains(existingId)) {
                opportunityRegionRepository.deleteByOpportunityIdAndRegionId(opportunityId, existingId);
            }
        }

        // 添加新传递的片区关联（如果原始片区在列表中，跳过）
        for (String regionId : regionIds) {
            // 跳过原始片区，因为原始片区不需要通过传递关联表来查询
            if (regionId.equals(ownerRegionId)) {
                continue;
            }

            // 检查是否已存在
            boolean exists = existingRegions.stream()
                    .anyMatch(or -> or.getRegionId().equals(regionId));

            if (!exists) {
                Optional<SalesRegion> regionOpt = salesRegionRepository.findById(regionId);
                if (regionOpt.isPresent()) {
                    OpportunityRegion or = new OpportunityRegion();
                    or.setOpportunityId(opportunityId);
                    or.setRegionId(regionId);
                    or.setRegionName(regionOpt.get().getRegionName());
                    opportunityRegionRepository.save(or);
                }
            }
        }
    }

    @Transactional
    public void assignEmployees(String opportunityId, List<String> employeeIds) {
        // 删除原有员工关联
        opportunityEmployeeRepository.deleteByOpportunityId(opportunityId);

        // 添加新员工关联
        for (String employeeId : employeeIds) {
            Optional<SalesPerson> employeeOpt = salesPersonRepository.findById(employeeId);
            if (employeeOpt.isPresent()) {
                OpportunityEmployee oe = new OpportunityEmployee();
                oe.setOpportunityId(opportunityId);
                oe.setEmployeeId(employeeId);
                oe.setEmployeeName(employeeOpt.get().getName());
                opportunityEmployeeRepository.save(oe);
            }
        }
    }

    @Transactional
    public void close(String opportunityId, String closeReason, String roleId) {
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(opportunityId);
        if (!opt.isPresent()) {
            throw new RuntimeException("销售机会不存在");
        }

        // 只有系统管理员和超级管理员可以关闭机会
        if (!"r001".equals(roleId) && !"r002".equals(roleId)) {
            throw new RuntimeException("无权限关闭销售机会");
        }

        ProjectOpportunity opportunity = opt.get();
        opportunity.setStatus("已关闭");
        opportunity.setRemarks((opportunity.getRemarks() != null ? opportunity.getRemarks() + "\n" : "") +
                "关闭原因：" + closeReason);
        projectOpportunityRepository.save(opportunity);
    }

    public List<OpportunityTracking> getTrackings(String opportunityId) {
        return opportunityTrackingRepository.findByOpportunityIdOrderByCreateTimeDesc(opportunityId);
    }

    @Transactional
    public OpportunityTracking saveTracking(OpportunityTracking tracking) {
        // 从销售机会中同步基本信息
        Optional<ProjectOpportunity> opt = projectOpportunityRepository.findById(tracking.getOpportunityId());
        if (opt.isPresent()) {
            ProjectOpportunity opportunity = opt.get();
            tracking.setOpportunityName(opportunity.getOpportunityName());
            tracking.setStage(opportunity.getStage());
            tracking.setCustomerId(opportunity.getCustomerId());
            tracking.setCustomerName(opportunity.getCustomerName());
            tracking.setBudget(opportunity.getBudget());
            tracking.setIndustry(opportunity.getIndustry());
            tracking.setSource(opportunity.getSource());
            tracking.setOpportunityDate(opportunity.getOpportunityDate());
        }
        return opportunityTrackingRepository.save(tracking);
    }

    @Transactional
    public void deleteTracking(String trackingId) {
        opportunityTrackingRepository.deleteById(trackingId);
    }

    public List<OpportunityRegion> getRegions(String opportunityId) {
        return opportunityRegionRepository.findByOpportunityId(opportunityId);
    }

    public List<OpportunityEmployee> getEmployees(String opportunityId) {
        return opportunityEmployeeRepository.findByOpportunityId(opportunityId);
    }

    public List<OpportunityKeyPerson> getKeyPersons(String opportunityId) {
        return opportunityKeyPersonRepository.findByOpportunityId(opportunityId);
    }

    @Transactional
    public void saveKeyPersons(String opportunityId, List<OpportunityKeyPerson> keyPersons) {
        // 删除原有关键人物
        opportunityKeyPersonRepository.deleteByOpportunityId(opportunityId);

        // 保存新关键人物
        for (OpportunityKeyPerson kp : keyPersons) {
            kp.setOpportunityId(opportunityId);
            opportunityKeyPersonRepository.save(kp);
        }
    }
}
