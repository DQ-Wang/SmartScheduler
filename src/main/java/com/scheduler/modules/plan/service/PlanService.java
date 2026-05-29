package com.scheduler.modules.plan.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.scheduler.common.enums.PlanStatus;
import com.scheduler.common.enums.RelationType;
import com.scheduler.common.exception.BusinessException;
import com.scheduler.modules.plan.convert.PlanConvert;
import com.scheduler.modules.plan.domain.PlanItemEntity;
import com.scheduler.modules.plan.domain.PlanResourceEntity;
import com.scheduler.modules.plan.dto.PlanItemCreateRequestDTO;
import com.scheduler.modules.plan.dto.PlanResourceRequestDTO;
import com.scheduler.modules.plan.repository.PlanItemRepository;
import com.scheduler.modules.plan.repository.PlanResourceRepository;
import com.scheduler.modules.plan.vo.PlanItemVO;
import com.scheduler.modules.schedule.domain.TimeSlotEntity;
import com.scheduler.modules.schedule.repository.TimeSlotRepository;
import com.scheduler.modules.schedule.service.ScheduleConflictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanItemRepository planItemRepository;
    private final PlanResourceRepository planResourceRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ScheduleConflictService scheduleConflictService;
    private final PlanConvert planConvert;

    @Transactional
    public PlanItemVO createPlanItem(PlanItemCreateRequestDTO request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BusinessException(400, "结束时间必须晚于开始时间");
        }
        scheduleConflictService.assertNoConflict(request.getStartTime(), request.getEndTime());

        PlanItemEntity entity = planConvert.toEntity(request);
        entity.setItemCode("PLAN-" + IdUtil.getSnowflakeNextIdStr());
        entity.setStatus(request.getStatus() == null ? PlanStatus.PENDING.name() : request.getStatus());
        PlanItemEntity saved = planItemRepository.save(entity);

        List<PlanResourceEntity> resources = saveResources(saved.getItemCode(), request.getResources());
        syncPlanTimeSlot(saved);

        PlanItemVO vo = planConvert.toVo(saved);
        vo.setResources(planConvert.toResourceVoList(resources));
        return vo;
    }

    private List<PlanResourceEntity> saveResources(String itemCode, List<PlanResourceRequestDTO> resources) {
        if (resources == null || resources.isEmpty()) {
            return List.of();
        }
        List<PlanResourceEntity> entities = new ArrayList<>();
        for (PlanResourceRequestDTO resource : resources) {
            if (StrUtil.isBlank(resource.getResourceName())) {
                continue;
            }
            PlanResourceEntity entity = new PlanResourceEntity();
            entity.setPlanItemCode(itemCode);
            entity.setResourceName(resource.getResourceName().trim());
            String url = resource.getResourceUrl();
            entity.setResourceUrl(StrUtil.isBlank(url) ? null : url.trim());
            entities.add(planResourceRepository.save(entity));
        }
        return entities;
    }

    private void syncPlanTimeSlot(PlanItemEntity plan) {
        TimeSlotEntity slot = new TimeSlotEntity();
        slot.setRelationType(RelationType.PLAN.name());
        slot.setRelationCode(plan.getItemCode());
        slot.setDayOfWeek(plan.getStartTime().getDayOfWeek().getValue());
        slot.setStartTime(plan.getStartTime().toLocalTime());
        slot.setEndTime(plan.getEndTime().toLocalTime());
        timeSlotRepository.save(slot);
    }
}
