package com.scheduler.modules.schedule.service;

import com.scheduler.common.enums.RelationType;
import com.scheduler.common.exception.BusinessException;
import com.scheduler.config.SchedulerProperties;
import com.scheduler.modules.exam.domain.ExamEntity;
import com.scheduler.modules.exam.repository.ExamRepository;
import com.scheduler.modules.plan.domain.PlanItemEntity;
import com.scheduler.modules.plan.repository.PlanItemRepository;
import com.scheduler.modules.schedule.domain.TimeInterval;
import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import com.scheduler.modules.schedule.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleConflictService {

    private final PlanItemRepository planItemRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ExamRepository examRepository;
    private final SchedulerProperties properties;

    public void assertNoConflict(LocalDateTime start, LocalDateTime end) {
        List<PlanItemEntity> planOverlaps = planItemRepository.findOverlapping(start, end);
        if (!planOverlaps.isEmpty()) {
            throw new BusinessException(409, "学习计划时间与已有计划冲突");
        }
        if (hasCourseOrExamConflict(start, end)) {
            throw new BusinessException(409, "学习计划时间与课程或考试安排冲突");
        }
    }

    private boolean hasCourseOrExamConflict(LocalDateTime start, LocalDateTime end) {
        TimeInterval target = new TimeInterval(start, end);
        LocalDate date = start.toLocalDate();
        while (!date.isAfter(end.toLocalDate())) {
            int dayOfWeek = date.getDayOfWeek().getValue();
            List<TimeSlotEntity> courseSlots = timeSlotRepository.findByRelationType(RelationType.COURSE.name());
            for (TimeSlotEntity slot : courseSlots) {
                if (slot.getDayOfWeek() != null && slot.getDayOfWeek().equals(dayOfWeek)) {
                    LocalDateTime slotStart = date.atTime(slot.getStartTime());
                    LocalDateTime slotEnd = date.atTime(slot.getEndTime());
                    if (target.overlaps(new TimeInterval(slotStart, slotEnd))) {
                        return true;
                    }
                }
            }
            date = date.plusDays(1);
        }
        int examMinutes = properties.getDefaultExamDurationMinutes();
        for (ExamEntity exam : examRepository.findAll()) {
            if (exam.getExamTime() == null) {
                continue;
            }
            LocalDateTime examStart = exam.getExamTime();
            LocalDateTime examEnd = examStart.plusMinutes(examMinutes);
            if (target.overlaps(new TimeInterval(examStart, examEnd))) {
                return true;
            }
        }
        return false;
    }
}
