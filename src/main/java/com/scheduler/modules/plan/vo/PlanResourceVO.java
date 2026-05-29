package com.scheduler.modules.plan.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanResourceVO {

    private String resourceName;
    private String resourceUrl;
}
