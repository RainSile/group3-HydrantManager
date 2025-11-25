package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.GeoDTO;
import g3.hydrantmana.domain.query.GeoHydrantQuery;
import g3.hydrantmana.domain.query.GeoMinDistanceQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.hydrantweb.service.GeoInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "地理信息")
@RestController
@RequestMapping("/geo")
public class GeoInfoController {

    @Resource
    GeoInfoService geoInfoService;

    @GetMapping("/min-distance")
    public JsonVO<GeoDTO> getGeoMinDistance(GeoMinDistanceQuery query) {
        GeoDTO geoDTO = geoInfoService.getGeoMinDistance(query);
        return JsonVO.success(geoDTO);
    }

    @GetMapping("/find-hydrant")
    public JsonVO<List<GeoDTO>> findHydrant(GeoHydrantQuery query) {
        List<GeoDTO> dtoList = geoInfoService.findHydrant(query);
        return JsonVO.success(dtoList);
    }
}
