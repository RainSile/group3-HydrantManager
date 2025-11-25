package g3.hydrantmana.hydrantweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.common.constants.DatabaseConstants;
import g3.hydrantmana.common.constants.HydrantStatusConstants;
import g3.hydrantmana.common.constants.SystemConstants;
import g3.hydrantmana.common.exceptions.FiledException;
import g3.hydrantmana.common.exceptions.RecordNotFoundException;
import g3.hydrantmana.domain.dto.*;
import g3.hydrantmana.domain.entity.Hydrant;
import g3.hydrantmana.domain.entity.Log;
import g3.hydrantmana.domain.entity.Maintenance;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.query.MaintenanceQuery;
import g3.hydrantmana.hydrantweb.mapper.HydrantMapper;
import g3.hydrantmana.hydrantweb.mapper.MaintenanceMapper;
import g3.hydrantmana.hydrantweb.service.GeoInfoService;
import g3.hydrantmana.hydrantweb.service.HydrantService;
import g3.hydrantmana.hydrantweb.service.MaintenanceService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 消防栓维护记录服务实现类
 *
 * @author Group3
 * @since 2025-11-24
 */
@Service
@Transactional
@Slf4j
public class MaintenanceServiceImpl implements MaintenanceService {

    @Resource
    private MaintenanceMapper maintenanceMapper;

    @Resource
    private HydrantService hydrantService;
    @Resource
    private HydrantMapper hydrantMapper;
    @Resource
    private GeoInfoService geoInfoService;

    @Override
    public PageDTO<MaintenanceDTO> listMaintenance(MaintenanceQuery query) {
        log.info("开始查询维护记录，查询条件：{}", query);
        Page<MaintenanceDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        maintenanceMapper.selectMaintenance(page, query);
        return PageDTO.of(page);
    }

    @Override
    public void addMaintenance(MaintenanceDTO maintenanceDTO) {
        geoInfoService.removeGeoInfo(maintenanceDTO.getHid());

        if (maintenanceDTO.getUid() == null || maintenanceDTO.getHid() == null) {
            throw new FiledException("无法建立映射关系: 字段不完整,uid,hid必须同时存在");
        }

        log.info("开始添加维护记录：{}", maintenanceDTO);
        Maintenance maintenance = new Maintenance();
        BeanUtils.copyProperties(maintenanceDTO, maintenance);
        maintenance.setDeleted(0);
        maintenanceMapper.insert(maintenance);

        Hydrant hydrant = hydrantMapper.selectById(maintenanceDTO.getHid());
        if (hydrant.getStatus() == HydrantStatusConstants.NORMAL || hydrant.getStatus() ==  HydrantStatusConstants.UNNORMAL) {
            HydrantDTO hydrantDTO = new HydrantDTO();
            String hid = hydrant.getId();
            hydrantDTO.setId(hid);
            hydrantDTO.setStatus(HydrantStatusConstants.MAINTENANCE);
            hydrantService.changeHydrant(hydrantDTO);
        }
    }

    @Override
    public void removeMaintenance(String id) {
        log.info("完成维护记录，ID：{}", id);

        Maintenance maintenance = maintenanceMapper.selectById(id);

        if (maintenance == null){
            throw new RecordNotFoundException("该条数据不存在");
        }
        if (maintenance.getDeleted() == SystemConstants.LOGICAL_DELETED) {
            throw new RecordNotFoundException("该数据已被删除");
        }
        maintenanceMapper.logicalDeleteById(id);

        String hid = maintenance.getHid();
        Hydrant hydrant = hydrantMapper.selectById(hid);
        if (hydrant.getStatus() == HydrantStatusConstants.MAINTENANCE) {
            HydrantDTO hydrantDTO = new HydrantDTO();
            hydrantDTO.setId(hid);
            hydrantDTO.setStatus(HydrantStatusConstants.NORMAL);
            hydrantService.changeHydrant(hydrantDTO);
        }
        GeoDTO geoDTO = new GeoDTO();
        geoDTO.setHid(hid);
        geoDTO.setLongitude(hydrant.getLongitude());
        geoDTO.setLatitude(hydrant.getLatitude());
        geoInfoService.addGeoInfo(geoDTO);
    }
}