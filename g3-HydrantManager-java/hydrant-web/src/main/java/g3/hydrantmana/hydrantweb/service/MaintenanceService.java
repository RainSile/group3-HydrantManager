package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.dto.MaintenanceDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.query.MaintenanceQuery;

/**
 * 消防栓维护记录服务接口
 * 提供维护记录和日志的业务操作方法
 * 严格对应Controller方法
 *
 * @author Group3
 * @since 2025-11-24
 */
public interface MaintenanceService {
    /**
     * 分页查询维护记录
     * 对应Controller: listMaintenance
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageDTO<MaintenanceDTO> listMaintenance(MaintenanceQuery query);

    /**
     * 添加维护记录
     * 对应Controller: addMaintenance
     *
     * @param maintenanceDTO 维护记录信息
     */
    void addMaintenance(MaintenanceDTO maintenanceDTO);

    /**
     * 完成维护记录
     * 对应Controller: removeMaintenance
     *
     * @param id 维护记录ID
     */
    void removeMaintenance(String id);
}