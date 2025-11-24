package g3.hydrantmana.hydrantweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.domain.entity.Maintenance;
import g3.hydrantmana.domain.entity.Log;
import g3.hydrantmana.domain.dto.MaintenanceDTO;
import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.query.MaintenanceQuery;
import g3.hydrantmana.domain.query.LogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 消防栓维护记录Mapper接口
 * 使用MyBatis-Plus进行数据访问
 * 包含Maintenance和Log的功能
 *
 * @author Group3
 * @since 2025-11-24
 */
@Mapper
public interface MaintenanceMapper extends BaseMapper<Maintenance> {

    /**
     * 分页查询维护记录
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<MaintenanceDTO> selectMaintenance(Page<MaintenanceDTO> page, @Param("query") MaintenanceQuery query);

    /**
     * 逻辑删除维修关系映射条目
     * @param id
     * @return
     */
    @Update("update maintenance_mapping set deleted = 1 where id = #{id}")
    int logicalDeleteById(String id);
}