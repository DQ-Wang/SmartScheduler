package com.scheduler.modules.course.vo;

import com.scheduler.modules.schedule.dto.TimeSlotDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDetailVO {

    private Long id;
    private String code;
    private String name;
    private String teacher;
    private String location;
    private List<String> aliases;
    private List<TimeSlotDTO> timeSlots;
}
