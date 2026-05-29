package com.scheduler.modules.plan.convert;

import com.scheduler.config.JacksonConfig;
import com.scheduler.modules.plan.domain.PlanItemEntity;
import com.scheduler.modules.plan.domain.PlanResourceEntity;
import com.scheduler.modules.plan.dto.PlanItemCreateRequestDTO;
import com.scheduler.modules.plan.vo.PlanItemVO;
import com.scheduler.modules.plan.vo.PlanResourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanConvert {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(JacksonConfig.DATE_TIME_PATTERN);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PlanItemEntity toEntity(PlanItemCreateRequestDTO request);

    PlanResourceVO toResourceVo(PlanResourceEntity entity);

    List<PlanResourceVO> toResourceVoList(List<PlanResourceEntity> entities);

    @Mapping(target = "startTime", expression = "java(formatDateTime(entity.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(formatDateTime(entity.getEndTime()))")
    @Mapping(target = "resources", ignore = true)
    PlanItemVO toVo(PlanItemEntity entity);

    default String formatDateTime(LocalDateTime time) {
        return time == null ? null : time.format(FORMATTER);
    }
}
