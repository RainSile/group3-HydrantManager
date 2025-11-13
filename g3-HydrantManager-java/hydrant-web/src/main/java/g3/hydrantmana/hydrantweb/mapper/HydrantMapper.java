package g3.hydrantmana.hydrantweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.query.HydrantQuery;
import g3.hydrantmana.domain.entity.Hydrant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HydrantMapper extends BaseMapper<Hydrant> {

    /**
     * 查询消防栓(条件+分页)
     * @param query
     * @return
     */
    Page<HydrantDTO> selectHydrant(Page<HydrantDTO> page,
                                   @Param("query") HydrantQuery query);

    /**
     * 更新消防栓
     * @param hydrantDTO
     * @return
     */
    int updateHydrant(@Param("dto") HydrantDTO hydrantDTO);
}
