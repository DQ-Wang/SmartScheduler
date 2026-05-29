package com.scheduler.modules.plan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlanResourceRequestDTO {

    @NotBlank(message = "资源名称不能为空")
    private String resourceName;

    /** 可选：无链接时仅保存资源名称 */
    private String resourceUrl;
}
