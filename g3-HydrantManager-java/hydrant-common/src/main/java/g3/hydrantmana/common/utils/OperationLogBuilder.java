package g3.hydrantmana.common.util;

import g3.hydrantmana.domain.dto.OperationLogDTO;

import java.time.LocalDateTime;

/**
 * 操作日志构建工具类
 * 用于统一生成操作日志对象
 */
public class OperationLogBuilder {

    private OperationLogBuilder() {
    }

    public static OperationLogDTO build(
            String operatorId,
            String module,
            String action,
            String description
    ) {
        OperationLogDTO log = new OperationLogDTO();
        log.setOperatorId(operatorId);
        log.setModule(module);
        log.setAction(action);
        log.setDescription(description);
        log.setOperateTime(LocalDateTime.now());
        return log;
    }
}
