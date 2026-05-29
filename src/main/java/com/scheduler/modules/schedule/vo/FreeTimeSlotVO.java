package com.scheduler.modules.schedule.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FreeTimeSlotVO {

    private String date;
    private String startTime;
    private String endTime;
    private long durationMinutes;
}
