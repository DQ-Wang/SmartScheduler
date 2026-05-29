package com.scheduler.modules.schedule.repository;

import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {

    List<TimeSlotEntity> findByRelationTypeAndRelationCode(String relationType, String relationCode);

    List<TimeSlotEntity> findByRelationType(String relationType);
}
