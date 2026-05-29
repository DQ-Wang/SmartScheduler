package com.scheduler.modules.mcp.dto;

import com.scheduler.modules.course.dto.CourseDictItemDTO;
import com.scheduler.modules.exam.dto.ExamDictItemDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DictItemsDTO {

    private List<CourseDictItemDTO> courses;
    private List<ExamDictItemDTO> exams;
}
