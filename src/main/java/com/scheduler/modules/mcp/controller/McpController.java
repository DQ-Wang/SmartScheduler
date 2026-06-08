package com.scheduler.modules.mcp.controller;

import com.scheduler.common.constant.ApiConstants;
import com.scheduler.common.enums.DetailType;
import com.scheduler.common.result.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.scheduler.modules.dify.dto.DifyWorkflowRunRequestDTO;
import com.scheduler.modules.mcp.dto.DictItemsDTO;
import com.scheduler.modules.mcp.service.McpFacadeService;
import com.scheduler.modules.plan.dto.PlanItemCreateRequestDTO;
import com.scheduler.modules.plan.vo.PlanItemVO;
import com.scheduler.modules.schedule.vo.FreeTimeSlotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @Operation(summary = "获取空闲时间", description = "扣除课程、考试、学习计划占用后，返回全天剩余空闲时段")
    @GetMapping("/schedule/free-time")
    public Result<List<FreeTimeSlotVO>> freeTime(
            @Parameter(description = "开始日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(mcpFacadeService.getFreeTime(startDate, endDate));
    }

    @Operation(summary = "获取学习计划", description = "按 itemCode 查询单条，或按日期范围查询重叠的计划列表")
    @GetMapping("/plan/items")
    public Result<List<PlanItemVO>> planItems(
            @Parameter(description = "计划编码，传入时忽略日期范围") @RequestParam(required = false) String itemCode,
            @Parameter(description = "开始日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 yyyy-MM-dd") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(mcpFacadeService.getPlanItems(startDate, endDate, itemCode));
    }

    @Operation(summary = "运行 Dify 工作流", description = "代理调用 Dify POST /v1/workflows/run，API Key 保存在服务端")
    @PostMapping("/dify/workflow/run")
    public Result<JsonNode> runDifyWorkflow(@Valid @RequestBody DifyWorkflowRunRequestDTO request) {
        return Result.success(mcpFacadeService.runDifyWorkflow(request));
    }

    @Operation(summary = "新增学习计划", description = "保存计划与资源，校验时间冲突并自动生成 item_code")
    @PostMapping("/plan/item")
    public Result<PlanItemVO> createPlanItem(@Valid @RequestBody PlanItemCreateRequestDTO request) {
        return Result.success(mcpFacadeService.createPlanItem(request));
    }

    @Operation(summary = "删除学习计划", description = "按 itemCode 物理删除计划、关联资源及时间槽")
    @DeleteMapping("/plan/item")
    public Result<PlanItemVO> deletePlanItem(
            @Parameter(description = "计划编码", required = true) @RequestParam @NotBlank(message = "itemCode 不能为空") String itemCode) {
        return Result.success(mcpFacadeService.deletePlanItem(itemCode));
    }
}
