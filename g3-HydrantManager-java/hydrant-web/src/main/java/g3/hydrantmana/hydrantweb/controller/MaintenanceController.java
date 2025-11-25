package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.dto.MaintenanceDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.query.MaintenanceQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.hydrantweb.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "维修管理")
@RestController
@RequestMapping("/maintenance")
@Slf4j
public class MaintenanceController {

    @Resource
    MaintenanceService maintenanceService;

    @Operation(tags = "维修管理",summary = "查询维修项(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<MaintenanceDTO>> listMaintenance(@Validated MaintenanceQuery query){
        PageDTO<MaintenanceDTO> pageDTO = maintenanceService.listMaintenance(query);
        return JsonVO.success(pageDTO);
    }

    @Operation(tags = "维修管理",summary = "新增维修项")
    @PostMapping("/add")
    public JsonVO addMaintenance(@RequestBody MaintenanceDTO maintenanceDTO){
        maintenanceService.addMaintenance(maintenanceDTO);
        return JsonVO.success("新增维修项成功");
    }

    @Operation(tags = "维修管理",summary = "完成维修项")
    @PutMapping("/remove/{id}")
    public JsonVO removeMaintenance(@PathVariable String id){
        maintenanceService.removeMaintenance(id);
        return JsonVO.success("完成维修项成功");
    }
}
