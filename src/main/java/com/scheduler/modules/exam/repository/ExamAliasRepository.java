package com.scheduler.modules.exam.repository;

import com.scheduler.modules.exam.domain.ExamAliasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAliasRepository extends JpaRepository<ExamAliasEntity, Long> {

    List<ExamAliasEntity> findByExamId(Long examId);

    List<ExamAliasEntity> findAllByOrderByIdAsc();
}
