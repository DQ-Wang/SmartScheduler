package com.scheduler.modules.schedule.service;

import com.scheduler.common.enums.RelationType;
import com.scheduler.config.JacksonConfig;
import com.scheduler.config.SchedulerProperties;
import com.scheduler.modules.exam.domain.ExamEntity;
import com.scheduler.modules.exam.repository.ExamRepository;
import com.scheduler.modules.plan.domain.PlanItemEntity;
import com.scheduler.modules.plan.repository.PlanItemRepository;
import com.scheduler.modules.schedule.domain.TimeInterval;
import com.scheduler.modules.schedule.domain.TimeIntervalMerger;
import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import com.scheduler.modules.schedule.repository.TimeSlotRepository;
import com.scheduler.modules.schedule.vo.FreeTimeSlotVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeTimeService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TimeSlotRepository timeSlotRepository;
    private final PlanItemRepository planItemRepository;
    private final ExamRepository examRepository;
    private final SchedulerProperties properties;

    @Transactional(readOnly = true)
    public List<FreeTimeSlotVO> calculateFreeTime(LocalDate startDate, LocalDate endDate) {
        LocalDate rangeStart = startDate != null ? startDate : LocalDate.now();
        LocalDate rangeEnd = endDate != null ? endDate : rangeStart.plusDays(6);
        if (rangeEnd.isBefore(rangeStart)) {
            throw new IllegalArgumentException("结束日期不能早于开始日期");
        }

        LocalTime workStart = LocalTime.parse(properties.getWorkDayStart());
        LocalTime workEnd = LocalTime.parse(properties.getWorkDayEnd());

        List<FreeTimeSlotVO> result = new ArrayList<>();
        for (LocalDate date = rangeStart; !date.isAfter(rangeEnd); date = date.plusDays(1)) {
            LocalDateTime dayWindowStart = date.atTime(workStart);
            LocalDateTime dayWindowEnd = date.atTime(workEnd);
            List<TimeInterval> busy = collectBusyIntervals(date, dayWindowStart, dayWindowEnd);
            List<TimeInterval> mergedBusy = TimeIntervalMerger.merge(busy);
            List<TimeInterval> freeIntervals = TimeIntervalMerger.subtract(dayWindowStart, dayWindowEnd, mergedBusy);
            for (TimeInterval interval : freeIntervals) {
                long minutes = ChronoUnit.MINUTES.between(interval.start(), interval.end());
                if (minutes <= 0) {
                    continue;
                }
                result.add(FreeTimeSlotVO.builder()
                        .date(date.format(DATE_FORMATTER))
                        .startTime(interval.start().format(JacksonConfig.DATE_TIME_FORMATTER))
                        .endTime(interval.end().format(JacksonConfig.DATE_TIME_FORMATTER))
                        .durationMinutes(minutes)
                        .build());
            }
        }
        return result;
    }

    private List<TimeInterval> collectBusyIntervals(LocalDate date,
                                                    LocalDateTime dayWindowStart,
                                                    LocalDateTime dayWindowEnd) {
        List<TimeInterval> busy = new ArrayList<>();
        int dayOfWeek = date.getDayOfWeek().getValue();

        List<TimeSlotEntity> courseSlots = timeSlotRepository.findByRelationType(RelationType.COURSE.name());
        for (TimeSlotEntity slot : courseSlots) {
            if (slot.getDayOfWeek() != null && slot.getDayOfWeek().equals(dayOfWeek)
                    && slot.getStartTime() != null && slot.getEndTime() != null) {
                LocalDateTime start = date.atTime(slot.getStartTime());
                LocalDateTime end = date.atTime(slot.getEndTime());
                busy.add(clampInterval(start, end, dayWindowStart, dayWindowEnd));
            }
        }

        List<PlanItemEntity> plans = planItemRepository.findByStartTimeBetween(
                dayWindowStart.minusDays(1), dayWindowEnd.plusDays(1));
        for (PlanItemEntity plan : plans) {
            if (plan.getStartTime() == null || plan.getEndTime() == null) {
                continue;
            }
            if (overlapsDay(plan.getStartTime(), plan.getEndTime(), date)) {
                busy.add(clampInterval(plan.getStartTime(), plan.getEndTime(), dayWindowStart, dayWindowEnd));
            }
        }

        List<ExamEntity> exams = examRepository.findAll();
        int examMinutes = properties.getDefaultExamDurationMinutes();
        for (ExamEntity exam : exams) {
            if (exam.getExamTime() == null) {
                continue;
            }
            LocalDateTime examStart = exam.getExamTime();
            LocalDateTime examEnd = examStart.plusMinutes(examMinutes);
            if (overlapsDay(examStart, examEnd, date)) {
                busy.add(clampInterval(examStart, examEnd, dayWindowStart, dayWindowEnd));
            }
        }
        return busy.stream().filter(interval -> interval != null).toList();
    }

    private boolean overlapsDay(LocalDateTime start, LocalDateTime end, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        return start.isBefore(dayEnd) && end.isAfter(dayStart);
    }

    private TimeInterval clampInterval(LocalDateTime start, LocalDateTime end,
                                       LocalDateTime windowStart, LocalDateTime windowEnd) {
        LocalDateTime clampedStart = start.isBefore(windowStart) ? windowStart : start;
        LocalDateTime clampedEnd = end.isAfter(windowEnd) ? windowEnd : end;
        if (!clampedEnd.isAfter(clampedStart)) {
            return null;
        }
        return new TimeInterval(clampedStart, clampedEnd);
    }
}
