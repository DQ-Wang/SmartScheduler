package com.scheduler.modules.exam.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ExamDetailVO {

    private Long id;
    private String code;
    private String name;
    private String examTime;
    private List<String> aliases;
}
