package org.example.permissionsystembackend.service;

import org.example.permissionsystembackend.dto.OptionDTO;
import org.example.permissionsystembackend.entity.Option;

import java.util.List;
import java.util.Map;

/**
 * 选项服务接口
 */
public interface OptionService {

    List<Option> getAllOptions();

    Map<String, List<Option>> getOptionsByGroup();

    List<Option> getOptionsByGroupName(String groupName);

    Option getOptionById(String id);

    Option createOption(OptionDTO dto);

    Option updateOption(String id, OptionDTO dto);

    void deleteOption(String id);
}
