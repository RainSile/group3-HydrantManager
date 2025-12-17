package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.ResponsibleUnitDTO;
import g3.hydrantmana.domain.query.HydrantQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.hydrantweb.service.ResponsibleUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "消防栓责任单位")
@RestController
@RequestMapping("/responsibleUnit")
@Slf4j
public class ResponsibleUnitController {

    @Resource
    private ResponsibleUnitService responsibleUnitService;

    @Operation(tags = "消防栓责任单位",summary = "查询责任单位(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<ResponsibleUnitDTO>> listHydrant(ResponsibleUnitDTO dto){
        PageDTO<ResponsibleUnitDTO> pageDTO = responsibleUnitService.listUnit(dto);
        return JsonVO.success(pageDTO);
    }

    @Operation(tags = "消防栓责任单位",summary = "联系责任单位")
    @GetMapping("/contact")
    public JsonVO addHydrant(ResponsibleUnitDTO responsibleUnitDTO){
        responsibleUnitService.contactUnit(responsibleUnitDTO);
        return JsonVO.success("添加成功");
    }

}
