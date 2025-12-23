package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.BiddingInfo;
import com.it.quanxianguanli.entity.ProjectOpportunity;
import com.it.quanxianguanli.repository.BiddingInfoRepository;
import com.it.quanxianguanli.repository.ProjectOpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingInfoService {

    @Autowired
    private BiddingInfoRepository biddingInfoRepository;
    
    @Autowired
    private ProjectOpportunityRepository projectOpportunityRepository;

    public List<BiddingInfo> findAll() {
        return biddingInfoRepository.findAll();
    }

    public List<BiddingInfo> findByConditions(String biddingName, String biddingType, 
                                               String biddingStatus, String customerName) {
        Specification<BiddingInfo> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (biddingName != null && !biddingName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("biddingName")), "%" + biddingName.toLowerCase() + "%"));
            }
            
            if (biddingType != null && !biddingType.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("biddingType"), biddingType));
            }
            
            if (biddingStatus != null && !biddingStatus.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("biddingStatus"), biddingStatus));
            }
            
            if (customerName != null && !customerName.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%"));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return biddingInfoRepository.findAll(spec);
    }

    public Optional<BiddingInfo> findById(String id) {
        return biddingInfoRepository.findById(id);
    }

    @Transactional
    public BiddingInfo save(BiddingInfo biddingInfo) {
        // 生成投标编号
        if (biddingInfo.getBiddingCode() == null || biddingInfo.getBiddingCode().isEmpty()) {
            biddingInfo.setBiddingCode("BID" + System.currentTimeMillis());
        }
        
        // 如果关联了销售机会，同步机会信息
        if (biddingInfo.getOpportunityId() != null && !biddingInfo.getOpportunityId().isEmpty()) {
            Optional<ProjectOpportunity> oppOpt = projectOpportunityRepository.findById(biddingInfo.getOpportunityId());
            if (oppOpt.isPresent()) {
                ProjectOpportunity opportunity = oppOpt.get();
                biddingInfo.setOpportunityName(opportunity.getOpportunityName());
                biddingInfo.setOpportunityCode(opportunity.getOpportunityCode());
                biddingInfo.setStage(opportunity.getStage());
                biddingInfo.setIndustry(opportunity.getIndustry());
                biddingInfo.setBudget(opportunity.getBudget());
                biddingInfo.setOpportunityDate(opportunity.getOpportunityDate());
                biddingInfo.setSource(opportunity.getSource());
                biddingInfo.setRegion(opportunity.getRegions());
            }
        }
        
        return biddingInfoRepository.save(biddingInfo);
    }

    @Transactional
    public void delete(String id) {
        biddingInfoRepository.deleteById(id);
    }

    @Transactional
    public void submitSummary(String id, String biddingResult, String biddingSummary, String attachment) {
        Optional<BiddingInfo> opt = biddingInfoRepository.findById(id);
        if (opt.isPresent()) {
            BiddingInfo biddingInfo = opt.get();
            biddingInfo.setBiddingResult(biddingResult);
            biddingInfo.setBiddingSummary(biddingSummary);
            if (attachment != null && !attachment.isEmpty()) {
                biddingInfo.setAttachment(attachment);
            }
            biddingInfo.setBiddingStatus("已归档");
            
            biddingInfoRepository.save(biddingInfo);
            
            // 根据投标结果更新销售机会状态
            if (biddingInfo.getOpportunityId() != null) {
                Optional<ProjectOpportunity> oppOpt = projectOpportunityRepository.findById(biddingInfo.getOpportunityId());
                if (oppOpt.isPresent()) {
                    ProjectOpportunity opportunity = oppOpt.get();
                    
                    // 废标或未中标时，销售机会状态自动改为"已关闭"
                    if ("废标".equals(biddingResult) || "未中".equals(biddingResult)) {
                        opportunity.setStatus("已关闭");
                        projectOpportunityRepository.save(opportunity);
                    }
                    // 已中标且录入合同后，销售机会状态自动改为"已关闭"（这个需要在合同创建时处理）
                    // 流标时，销售机会保留可重新跟踪（不改变状态）
                }
            }
        } else {
            throw new RuntimeException("投标信息不存在");
        }
    }
}

