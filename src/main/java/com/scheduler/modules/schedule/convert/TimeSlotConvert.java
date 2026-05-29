package com.scheduler.modules.schedule.convert;

import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import com.scheduler.modules.schedule.dto.TimeSlotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeSlotConvert {

    @Mapping(target = "startTime", expression = "java(entity.getStartTime() == null ? null : entity.getStartTime().toString())")
    @Mapping(target = "endTime", expression = "java(entity.getEndTime() == null ? null : entity.getEndTime().toString())")
    TimeSlotDTO toDto(TimeSlotEntity entity);

    List<TimeSlotDTO> toDtoList(List<TimeSlotEntity> entities);
}
