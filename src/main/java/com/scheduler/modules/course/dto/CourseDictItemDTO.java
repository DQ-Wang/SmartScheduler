package com.scheduler.modules.course.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDictItemDTO {

    private Long id;
    private String code;
    private String name;
    private List<String> aliases;
}
