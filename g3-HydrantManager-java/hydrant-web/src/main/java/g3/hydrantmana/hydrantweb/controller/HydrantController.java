package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.HydrantQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.hydrantweb.service.HydrantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "消防栓管理")
@RestController
@RequestMapping("/hydrant")
@Slf4j
@RequestMapping("/responsibleUnit")
public class HydrantController {
    @Resource
    HydrantService hydrantService;

    @Operation(tags = "消防栓管理",summary = "查询消防栓(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<HydrantDTO>> listHydrant(@Validated HydrantQuery query){
        log.info("查询消防栓责任单位信息"); // ✅ 新增日志
        PageDTO<HydrantDTO> pageDTO = hydrantService.listHydrant(query);
        return JsonVO.success(pageDTO);
    }

    @Operation(tags = "消防栓管理",summary = "新增消防栓")
    @PostMapping("/add")
    public JsonVO addHydrant(@RequestBody HydrantDTO hydrantDTO){
        hydrantService.addHydrant(hydrantDTO);
        return JsonVO.success("添加成功");
    }

    @Operation(tags = "消防栓管理",summary = "删除消防栓")
    @DeleteMapping("/remove/{id}")
    public JsonVO removeHydrant(@PathVariable String id){
        hydrantService.removeHydrant(id);
        return JsonVO.success("删除成功");
    }

    @Operation(tags = "消防栓管理",summary = "更新消防栓")
    @PutMapping("/change")
    public JsonVO changeHydrant(@RequestBody HydrantDTO hydrantDTO){
        hydrantService.changeHydrant(hydrantDTO);
        return JsonVO.success("更新成功");
    }
}
