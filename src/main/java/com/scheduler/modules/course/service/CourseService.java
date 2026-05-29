package com.scheduler.modules.course.service;

import com.scheduler.common.enums.RelationType;
import com.scheduler.common.exception.BusinessException;
import com.scheduler.modules.course.domain.CourseAliasEntity;
import com.scheduler.modules.course.domain.CourseEntity;
import com.scheduler.modules.course.dto.CourseDictItemDTO;
import com.scheduler.modules.course.repository.CourseAliasRepository;
import com.scheduler.modules.course.repository.CourseRepository;
import com.scheduler.modules.course.vo.CourseDetailVO;
import com.scheduler.modules.schedule.convert.TimeSlotConvert;
import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import com.scheduler.modules.schedule.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseAliasRepository courseAliasRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotConvert timeSlotConvert;

    @Transactional(readOnly = true)
    public List<CourseDictItemDTO> listDictItems() {
        List<CourseEntity> courses = courseRepository.findAll();
        Map<Long, List<String>> aliasMap = courseAliasRepository.findAllByOrderByIdAsc().stream()
                .collect(Collectors.groupingBy(
                        CourseAliasEntity::getCourseId,
                        Collectors.mapping(CourseAliasEntity::getAliasName, Collectors.toList())));
        return courses.stream()
                .map(course -> CourseDictItemDTO.builder()
                        .id(course.getId())
                        .code(course.getCourseCode())
                        .name(course.getStandardName())
                        .aliases(aliasMap.getOrDefault(course.getId(), Collections.emptyList()))
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseDetailVO getDetail(Long id) {
        CourseEntity course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessException("课程不存在"));
        List<String> aliases = courseAliasRepository.findByCourseId(course.getId()).stream()
                .map(CourseAliasEntity::getAliasName)
                .toList();
        List<TimeSlotEntity> slots = timeSlotRepository.findByRelationTypeAndRelationCode(
                RelationType.COURSE.name(), course.getCourseCode());
        return CourseDetailVO.builder()
                .id(course.getId())
                .code(course.getCourseCode())
                .name(course.getStandardName())
                .teacher(course.getTeacher())
                .location(course.getLocation())
                .aliases(aliases)
                .timeSlots(timeSlotConvert.toDtoList(slots))
                .build();
    }
}
