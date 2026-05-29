package com.scheduler.modules.course.repository;

import com.scheduler.modules.course.domain.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    Optional<CourseEntity> findByCourseCode(String courseCode);
}
