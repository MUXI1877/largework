package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.BasicInfo;
import com.it.quanxianguanli.repository.BasicInfoRepository;
import com.it.quanxianguanli.dto.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BasicInfoService {

    @Autowired
    private BasicInfoRepository basicInfoRepository;

    @Transactional(readOnly = true)
    public List<BasicInfo> findAll() {
        return basicInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BasicInfo> findById(String id) {
        return basicInfoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<BasicInfo> findByCategory(String category) {
        return basicInfoRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<BasicInfo> findByCategoryAndEnabled(String category) {
        return basicInfoRepository.findByCategoryAndIsEnabledTrue(category);
    }

    @Transactional(readOnly = true)
    public Optional<BasicInfo> findByCode(String code) {
        return basicInfoRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<BasicInfo> findByParentId(String parentId) {
        return basicInfoRepository.findByParent_Id(parentId);
    }

    @Transactional
    public BasicInfo save(BasicInfo basicInfo) {
        return basicInfoRepository.save(basicInfo);
    }

    @Transactional
    public void deleteById(String id) {
        basicInfoRepository.deleteById(id);
    }

    @Transactional
    public BasicInfo update(BasicInfo basicInfo) {
        Optional<BasicInfo> existing = basicInfoRepository.findById(basicInfo.getId());
        if (existing.isPresent()) {
            return basicInfoRepository.save(basicInfo);
        } else {
            throw new RuntimeException("基本信息不存在");
        }
    }

    @Transactional
    public void toggleEnabled(String id, boolean enabled) {
        BasicInfo basicInfo = basicInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("基本信息不存在"));
        basicInfo.setIsEnabled(enabled);
        basicInfoRepository.save(basicInfo);
    }

    @Transactional(readOnly = true)
    public List<TreeNode> getCategoryTree(String category) {
        List<BasicInfo> basicInfos = basicInfoRepository.findByCategory(category);
        return buildTree(basicInfos);
    }

    @Transactional(readOnly = true)
    public List<TreeNode> getCategoryTreeByEnabled(String category) {
        List<BasicInfo> basicInfos = basicInfoRepository.findByCategoryAndIsEnabledTrue(category);
        return buildTree(basicInfos);
    }

    private List<TreeNode> buildTree(List<BasicInfo> basicInfos) {
        List<TreeNode> allNodes = basicInfos.stream().map(TreeNode::new).toList();

        // 首先标记哪些节点是父节点
        for (TreeNode node : allNodes) {
            String id = node.getId();
            boolean hasChildren = allNodes.stream().anyMatch(child -> id.equals(child.getParentId()));
            node.setIsParent(hasChildren);
        }

        // 构建树结构
        List<TreeNode> rootNodes = allNodes.stream()
                .filter(node -> node.getParentId() == null)
                .toList();

        for (TreeNode root : rootNodes) {
            buildChildren(root, allNodes);
        }

        return rootNodes;
    }

    private void buildChildren(TreeNode parent, List<TreeNode> allNodes) {
        List<TreeNode> children = allNodes.stream()
                .filter(node -> parent.getId().equals(node.getParentId()))
                .toList();

        for (TreeNode child : children) {
            buildChildren(child, allNodes);
        }

        parent.setChildren(children);
    }
}