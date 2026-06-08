package com.scheduler.modules.plan.repository;

import com.scheduler.modules.plan.domain.PlanResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PlanResourceRepository extends JpaRepository<PlanResourceEntity, Long> {

    List<PlanResourceEntity> findByPlanItemCode(String planItemCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM PlanResourceEntity e WHERE e.planItemCode = :planItemCode")
    void deleteByPlanItemCode(@Param("planItemCode") String planItemCode);
}
