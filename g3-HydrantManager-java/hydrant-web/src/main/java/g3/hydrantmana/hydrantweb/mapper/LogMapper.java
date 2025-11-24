package g3.hydrantmana.hydrantweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.entity.Log;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.vo.LogVO;
import org.apache.ibatis.annotations.Param;

public interface LogMapper extends BaseMapper<Log> {
    /**
     * 分页查询日志记录
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    Page<LogVO> selectLog(Page<LogDTO> page, @Param("query") LogQuery query);
}
