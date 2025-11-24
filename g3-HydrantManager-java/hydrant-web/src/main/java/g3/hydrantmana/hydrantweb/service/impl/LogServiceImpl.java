package g3.hydrantmana.hydrantweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.common.constants.DatabaseConstants;
import g3.hydrantmana.common.exceptions.RecordNotFoundException;
import g3.hydrantmana.domain.dto.LogDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.entity.Log;
import g3.hydrantmana.domain.query.LogQuery;
import g3.hydrantmana.domain.vo.LogVO;
import g3.hydrantmana.hydrantweb.mapper.LogMapper;
import g3.hydrantmana.hydrantweb.service.LogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class LogServiceImpl implements LogService {

    @Resource
    LogMapper logMapper;

    @Override
    public PageDTO<LogVO> listLog(LogQuery query) {
        log.info("开始查询日志记录，查询条件：{}", query);
        Page<LogDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        Page<LogVO> logVOPage = logMapper.selectLog(page, query);
        return PageDTO.of(logVOPage);
    }

    @Override
    public void addLog(LogDTO logDTO) {
        log.info("开始添加日志记录：{}", logDTO);
        Log log = new Log();
        BeanUtils.copyProperties(logDTO, log);
        logMapper.insert(log);
        return;
    }

    @Override
    public void removeLog(String id) {
        int affectedRows = logMapper.deleteById(id);
        if (affectedRows == DatabaseConstants.AffectedRows.ZERO){
            throw new RecordNotFoundException("数据未找到,可能不存在该条数据");
        }
    }
}
