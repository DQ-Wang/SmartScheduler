package com.scheduler.modules.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeSlotDTO {

    private Integer dayOfWeek;
    private String startTime;
    private String endTime;
}
