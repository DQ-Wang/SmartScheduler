package com.scheduler.modules.schedule.domain;

import java.time.LocalDateTime;

public record TimeInterval(LocalDateTime start, LocalDateTime end) {

    public TimeInterval {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("无效时间区间");
        }
    }

    public boolean overlaps(TimeInterval other) {
        return start.isBefore(other.end) && end.isAfter(other.start);
    }
}
