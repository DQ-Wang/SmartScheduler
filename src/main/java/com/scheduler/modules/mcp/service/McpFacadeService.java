package com.scheduler.modules.mcp.service;

import com.scheduler.common.enums.DetailType;
import com.scheduler.common.exception.BusinessException;
import com.scheduler.modules.course.service.CourseService;
import com.scheduler.modules.exam.service.ExamService;
import com.scheduler.modules.mcp.dto.DictItemsDTO;
import com.scheduler.modules.plan.dto.PlanItemCreateRequestDTO;
import com.scheduler.modules.plan.service.PlanService;
import com.scheduler.modules.plan.vo.PlanItemVO;
import com.scheduler.modules.schedule.service.FreeTimeService;
import com.scheduler.modules.schedule.vo.FreeTimeSlotVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class McpFacadeService {

    private final CourseService courseService;
    private final ExamService examService;
    private final FreeTimeService freeTimeService;
    private final PlanService planService;

    public DictItemsDTO getDictItems() {
        return DictItemsDTO.builder()
                .courses(courseService.listDictItems())
                .exams(examService.listDictItems())
                .build();
    }

    public Object getDetail(Long id, DetailType type) {
        if (type == null) {
            throw new BusinessException(400, "type 参数不能为空");
        }
        return switch (type) {
            case COURSE -> courseService.getDetail(id);
            case EXAM -> examService.getDetail(id);
        };
    }

    public List<FreeTimeSlotVO> getFreeTime(LocalDate startDate, LocalDate endDate) {
        List<FreeTimeSlotVO> slots = freeTimeService.calculateFreeTime(startDate, endDate);
        return slots == null ? Collections.emptyList() : slots;
    }

    public PlanItemVO createPlanItem(PlanItemCreateRequestDTO request) {
        return planService.createPlanItem(request);
    }
}
