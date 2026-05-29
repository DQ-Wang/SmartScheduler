package com.scheduler.modules.exam.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExamDictItemDTO {

    private Long id;
    private String code;
    private String name;
    private List<String> aliases;
}
