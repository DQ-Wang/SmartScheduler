package com.scheduler.modules.exam.service;

import com.scheduler.common.exception.BusinessException;
import com.scheduler.config.JacksonConfig;
import com.scheduler.modules.exam.domain.ExamAliasEntity;
import com.scheduler.modules.exam.domain.ExamEntity;
import com.scheduler.modules.exam.dto.ExamDictItemDTO;
import com.scheduler.modules.exam.repository.ExamAliasRepository;
import com.scheduler.modules.exam.repository.ExamRepository;
import com.scheduler.modules.exam.vo.ExamDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamAliasRepository examAliasRepository;

    @Transactional(readOnly = true)
    public List<ExamDictItemDTO> listDictItems() {
        List<ExamEntity> exams = examRepository.findAll();
        Map<Long, List<String>> aliasMap = examAliasRepository.findAllByOrderByIdAsc().stream()
                .collect(Collectors.groupingBy(
                        ExamAliasEntity::getExamId,
                        Collectors.mapping(ExamAliasEntity::getAliasName, Collectors.toList())));
        return exams.stream()
                .map(exam -> ExamDictItemDTO.builder()
                        .id(exam.getId())
                        .code(exam.getExamCode())
                        .name(exam.getStandardName())
                        .aliases(aliasMap.getOrDefault(exam.getId(), Collections.emptyList()))
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public ExamDetailVO getDetail(Long id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException("考试不存在"));
        List<String> aliases = examAliasRepository.findByExamId(exam.getId()).stream()
                .map(ExamAliasEntity::getAliasName)
                .toList();
        String examTime = exam.getExamTime() == null
                ? null
                : exam.getExamTime().format(JacksonConfig.DATE_TIME_FORMATTER);
        return ExamDetailVO.builder()
                .id(exam.getId())
                .code(exam.getExamCode())
                .name(exam.getStandardName())
                .examTime(examTime)
                .aliases(aliases)
                .build();
    }
}
