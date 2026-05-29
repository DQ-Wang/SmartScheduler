package com.scheduler.modules.plan.repository;

import com.scheduler.modules.plan.domain.PlanResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanResourceRepository extends JpaRepository<PlanResourceEntity, Long> {

    List<PlanResourceEntity> findByPlanItemCode(String planItemCode);
}
