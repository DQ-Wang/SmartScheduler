package com.scheduler.modules.mcp.controller;

import com.scheduler.common.constant.ApiConstants;
import com.scheduler.common.enums.DetailType;
import com.scheduler.common.result.Result;
import com.scheduler.modules.mcp.dto.DictItemsDTO;
import com.scheduler.modules.mcp.service.McpFacadeService;
import com.scheduler.modules.plan.dto.PlanItemCreateRequestDTO;
import com.scheduler.modules.plan.vo.PlanItemVO;
import com.scheduler.modules.schedule.vo.FreeTimeSlotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "MCP API", description = "Dify / AI Agent 调用的智能学习调度接口")
@Validated
@RestController
@RequestMapping(ApiConstants.API_PREFIX)
@RequiredArgsConstructor
public class McpController {

    private final McpFacadeService mcpFacadeService;

    @Operation(summary = "获取字典", description = "返回课程、考试及别名列表")
    @GetMapping("/dict/items")
    public Result<DictItemsDTO> dictItems() {
        return Result.success(mcpFacadeService.getDictItems());
    }

    @Operation(summary = "获取详情", description = "根据 id 与 type 查询课程或考试详情")
    @GetMapping("/info/detail")
    public Result<Object> detail(
            @Parameter(description = "业务主键 ID", required = true) @RequestParam Long id,
            @Parameter(description = "类型：COURSE / EXAM", required = true) @RequestParam DetailType type) {
        return Result.success(mcpFacadeService.getDetail(id, type));
    }

    @Operation(summary = "获取空闲时间", description = "根据课程、考试、学习计划动态计算空闲时段")
    @GetMapping("/schedule/free-time")
    public Result<List<FreeTimeSlotVO>> freeTime(
            @Parameter(description = "开始日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(mcpFacadeService.getFreeTime(startDate, endDate));
    }

    @Operation(summary = "新增学习计划", description = "保存计划与资源，校验时间冲突并自动生成 item_code")
    @PostMapping("/plan/item")
    public Result<PlanItemVO> createPlanItem(@Valid @RequestBody PlanItemCreateRequestDTO request) {
        return Result.success(mcpFacadeService.createPlanItem(request));
    }
}
