package com.scheduler.modules.exam.repository;

import com.scheduler.modules.exam.domain.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

    Optional<ExamEntity> findByExamCode(String examCode);
}
