package com.scheduler.modules.plan.repository;

import com.scheduler.modules.plan.domain.PlanItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlanItemRepository extends JpaRepository<PlanItemEntity, Long> {

    Optional<PlanItemEntity> findByItemCode(String itemCode);

    @Query("""
            SELECT p FROM PlanItemEntity p
            WHERE p.status <> 'CANCELLED'
              AND p.startTime < :endTime
              AND p.endTime > :startTime
            """)
    List<PlanItemEntity> findOverlapping(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    List<PlanItemEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
