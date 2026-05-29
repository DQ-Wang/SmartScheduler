package com.scheduler.modules.plan.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlanItemVO {

    private String itemCode;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String status;
    private List<PlanResourceVO> resources;
}
