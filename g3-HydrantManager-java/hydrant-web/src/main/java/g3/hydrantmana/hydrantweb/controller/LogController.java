package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.domain.vo.LogVO;
import g3.hydrantmana.hydrantweb.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "日志管理")
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {

    @Resource
    private LogService logService;

    @Operation(tags = "日志管理",summary = "查询维修日志(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<LogVO>> listLog(@Validated LogQuery query){
        PageDTO<LogVO> pageDTO = logService.listLog(query);
        return JsonVO.success(pageDTO);
    }

    @Operation(tags = "日志管理",summary = "维修项添加维修日志")
    @PostMapping("/add")
    public JsonVO addLog(@RequestBody LogDTO logDTO){
        logService.addLog(logDTO);
        return JsonVO.success("维修项添加维修日志成功");
    }

    @Operation(tags = "日志管理",summary = "删除某条维修日志")
    @DeleteMapping("/remove/{id}")
    public JsonVO removeLog(@PathVariable String id){
        logService.removeLog(id);
        return JsonVO.success("删除维修日志成功");
    }
}
