package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysOption;
import com.it.quanxianguanli.repository.SysOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysOptionService {

    @Autowired
    private SysOptionRepository optionRepository;

    public List<SysOption> findAll() {
        return optionRepository.findAll();
    }

    public List<SysOption> findByGroup(String group) {
        return optionRepository.findByGroupOrderBySortAsc(group);
    }

    public SysOption findById(String id) {
        return optionRepository.findById(id).orElse(null);
    }

    @Transactional
    public SysOption save(SysOption option) {
        return optionRepository.save(option);
    }

    @Transactional
    public void deleteById(String id) {
        optionRepository.deleteById(id);
    }
}
