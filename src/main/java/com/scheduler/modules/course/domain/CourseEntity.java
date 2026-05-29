package com.scheduler.modules.course.domain;

import com.scheduler.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class CourseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", nullable = false, unique = true, length = 64)
    private String courseCode;

    @Column(name = "standard_name")
    private String standardName;

    @Column(name = "teacher", length = 128)
    private String teacher;

    @Column(name = "location", length = 255)
    private String location;
}
