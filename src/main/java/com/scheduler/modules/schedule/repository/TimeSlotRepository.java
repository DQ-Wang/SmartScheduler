package com.scheduler.modules.schedule.repository;

import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {

    List<TimeSlotEntity> findByRelationTypeAndRelationCode(String relationType, String relationCode);

    List<TimeSlotEntity> findByRelationType(String relationType);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE FROM TimeSlotEntity e WHERE e.relationType = :relationType AND e.relationCode = :relationCode")
    void deleteByRelationTypeAndRelationCode(
            @Param("relationType") String relationType,
            @Param("relationCode") String relationCode);
}
