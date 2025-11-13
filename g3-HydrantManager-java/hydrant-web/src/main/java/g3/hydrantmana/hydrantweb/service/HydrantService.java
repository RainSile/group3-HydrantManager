package g3.hydrantmana.hydrantweb.service;


import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.HydrantQuery;

import java.util.List;

public interface HydrantService {

    /**
     * 查询消防栓(条件+分页)
     * @param query
     */
    PageDTO<HydrantDTO> listHydrant(HydrantQuery query);

    /**
     * 添加消防栓
     * @param hydrantDTO
     */
    void addHydrant(HydrantDTO hydrantDTO);

    /**
     * 删除消防栓
     * @param id
     */
    void removeHydrant(String id);

    /**
     * 更新消防栓
     * @param hydrantDTO
     */
    void changeHydrant(HydrantDTO hydrantDTO);
}
