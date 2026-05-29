package com.scheduler.modules.schedule.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 时间区间合并算法：用于空闲时间计算前的占用区间归并。
 */
public final class TimeIntervalMerger {

    private TimeIntervalMerger() {
    }

    public static List<TimeInterval> merge(List<TimeInterval> intervals) {
        if (intervals == null || intervals.isEmpty()) {
            return List.of();
        }
        List<TimeInterval> sorted = intervals.stream()
                .sorted(Comparator.comparing(TimeInterval::start))
                .toList();
        List<TimeInterval> merged = new ArrayList<>();
        TimeInterval current = sorted.getFirst();
        for (int i = 1; i < sorted.size(); i++) {
            TimeInterval next = sorted.get(i);
            if (!next.start().isAfter(current.end())) {
                LocalDateTime newEnd = next.end().isAfter(current.end()) ? next.end() : current.end();
                current = new TimeInterval(current.start(), newEnd);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }

    public static List<TimeInterval> subtract(LocalDateTime windowStart,
                                              LocalDateTime windowEnd,
                                              List<TimeInterval> busyMerged) {
        List<TimeInterval> free = new ArrayList<>();
        LocalDateTime cursor = windowStart;
        for (TimeInterval busy : busyMerged) {
            if (busy.start().isAfter(cursor)) {
                free.add(new TimeInterval(cursor, busy.start().isBefore(windowEnd) ? busy.start() : windowEnd));
            }
            if (busy.end().isAfter(cursor)) {
                cursor = busy.end();
            }
            if (!cursor.isBefore(windowEnd)) {
                break;
            }
        }
        if (cursor.isBefore(windowEnd)) {
            free.add(new TimeInterval(cursor, windowEnd));
        }
        return free.stream()
                .filter(slot -> slot.end().isAfter(slot.start()))
                .toList();
    }
}
