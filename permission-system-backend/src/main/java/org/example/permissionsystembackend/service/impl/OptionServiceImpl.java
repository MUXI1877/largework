package org.example.permissionsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.BusinessException;
import org.example.permissionsystembackend.common.ResultCode;
import org.example.permissionsystembackend.dto.OptionDTO;
import org.example.permissionsystembackend.entity.Option;
import org.example.permissionsystembackend.repository.OptionRepository;
import org.example.permissionsystembackend.service.OptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 选项服务实现类
 */
@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    public List<Option> getAllOptions() {
        return optionRepository.findAllByOrderByGroupNameAscSortOrderAsc();
    }

    @Override
    public Map<String, List<Option>> getOptionsByGroup() {
        List<Option> options = optionRepository.findAllByOrderByGroupNameAscSortOrderAsc();
        return options.stream().collect(Collectors.groupingBy(Option::getGroupName));
    }

    @Override
    public List<Option> getOptionsByGroupName(String groupName) {
        return optionRepository.findByGroupNameOrderBySortOrder(groupName);
    }

    @Override
    public Option getOptionById(String id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND));
    }

    @Override
    @Transactional
    public Option createOption(OptionDTO dto) {
        Option option = new Option();
        BeanUtils.copyProperties(dto, option);
        return optionRepository.save(option);
    }

    @Override
    @Transactional
    public Option updateOption(String id, OptionDTO dto) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND));

        option.setGroupName(dto.getGroupName());
        option.setTitle(dto.getTitle());
        option.setValue(dto.getValue());
        option.setSortOrder(dto.getSortOrder());
        return optionRepository.save(option);
    }

    @Override
    @Transactional
    public void deleteOption(String id) {
        if (!optionRepository.existsById(id)) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        optionRepository.deleteById(id);
    }
}
