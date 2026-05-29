package com.scheduler.modules.course.repository;

import com.scheduler.modules.course.domain.CourseAliasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseAliasRepository extends JpaRepository<CourseAliasEntity, Long> {

    List<CourseAliasEntity> findByCourseId(Long courseId);

    List<CourseAliasEntity> findAllByOrderByIdAsc();
}
