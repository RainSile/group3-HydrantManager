package g3.hydrantmana.hydrantweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.common.constants.DatabaseConstants;
import g3.hydrantmana.common.constants.HydrantStatusConstants;
import g3.hydrantmana.common.exceptions.OperationFailedException;
import g3.hydrantmana.common.exceptions.RecordNotFoundException;
import g3.hydrantmana.domain.dto.GeoDTO;
import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.entity.Hydrant;
import g3.hydrantmana.domain.query.HydrantQuery;
import g3.hydrantmana.hydrantweb.mapper.HydrantMapper;
import g3.hydrantmana.hydrantweb.service.GeoInfoService;
import g3.hydrantmana.hydrantweb.service.HydrantService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HydrantServiceImpl implements HydrantService {
    @Resource
    HydrantMapper hydrantMapper;

    @Resource
    GeoInfoService geoInfoService;

    /**
     * 查询消防栓(条件+分页)
     * @param query
     * @return
     */
    @Override
    public PageDTO<HydrantDTO> listHydrant(HydrantQuery query) {
        Page<HydrantDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        hydrantMapper.selectHydrant(page, query);
        return PageDTO.of(page);
    }

    /**
     * 添加消防栓
     * @param hydrantDTO
     */
    @Override
    public void addHydrant(HydrantDTO hydrantDTO) {
        if (hydrantDTO.getStatus() != null) {
            if (hydrantDTO.getStatus() == HydrantStatusConstants.NORMAL){
                GeoDTO geoDTO = new GeoDTO();
                geoDTO.setLatitude(hydrantDTO.getLatitude());
                geoDTO.setLongitude(hydrantDTO.getLongitude());
                geoDTO.setHid(hydrantDTO.getId());
                geoInfoService.addGeoInfo(geoDTO);
            }else{
                geoInfoService.removeGeoInfo(hydrantDTO.getId());
            }
        }
        Hydrant hydrant = new Hydrant();
        BeanUtils.copyProperties(hydrantDTO,hydrant);
        hydrantMapper.insert(hydrant);
        return;
    }

    /**
     * 移除消防栓
     * @param id
     */
    @Override
    public void removeHydrant(String id) {
        geoInfoService.removeGeoInfo(id);
        int affectedRows = hydrantMapper.deleteById(id);
        if (affectedRows == DatabaseConstants.AffectedRows.ZERO){
            throw new RecordNotFoundException("数据未找到,可能不存在该条数据");
        }
    }

    /**
     * 更新消防栓
     * @param hydrantDTO
     */
    @Override
    public void changeHydrant(HydrantDTO hydrantDTO) {
        if (hydrantDTO.getStatus() != null) {
            Hydrant hydrant = hydrantMapper.selectById(hydrantDTO.getId());

            if (hydrantDTO.getStatus() == HydrantStatusConstants.NORMAL){
                GeoDTO geoDTO = new GeoDTO();
                geoDTO.setLatitude(hydrant.getLatitude());
                geoDTO.setLongitude(hydrant.getLongitude());
                geoDTO.setHid(hydrant.getId());
                geoInfoService.addGeoInfo(geoDTO);
            }else{

                geoInfoService.removeGeoInfo(hydrant.getId());
            }
        }
        int affectedRows = hydrantMapper.updateHydrant(hydrantDTO);
        if (affectedRows == DatabaseConstants.AffectedRows.ZERO){
            throw new RecordNotFoundException("数据未找到,可能不存在该条数据");
        }
    }
}
