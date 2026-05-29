package com.scheduler.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "scheduler")
public class SchedulerProperties {

    private String timezone = "Asia/Shanghai";
    private String workDayStart = "08:00";
    private String workDayEnd = "22:00";
    private int defaultExamDurationMinutes = 120;
}
