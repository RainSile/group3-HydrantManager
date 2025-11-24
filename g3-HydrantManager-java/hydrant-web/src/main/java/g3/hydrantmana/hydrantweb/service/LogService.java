package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.vo.LogVO;

public interface LogService {

    /**
     * 分页查询日志记录
     * 对应Controller: listLog
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageDTO<LogVO> listLog(LogQuery query);

    /**
     * 添加日志记录
     * 对应Controller: addLog
     *
     * @param logDTO 日志记录信息
     */
    void addLog(LogDTO logDTO);

    /**
     * 删除日志记录
     * 对应Controller: removeLog
     *
     * @param id 日志记录ID
     */
    void removeLog(String id);
}
