package org.example.permissionsystembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 选项DTO
 */
@Data
public class OptionDTO {

    private String id;

    @NotBlank(message = "分组名不能为空")
    private String groupName;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "选项值不能为空")
    private String value;

    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;
}
