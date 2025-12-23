package g3.hydrantmana.common.handler;

import g3.hydrantmana.common.context.ApiResult;
import g3.hydrantmana.common.exceptions.BaseException;
import g3.hydrantmana.common.exceptions.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BaseException.class)
    public ApiResult<Void> handleBaseException(BaseException e) {
        log.warn("业务异常：{}", e.getMessage());
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理系统未知异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResult.fail(
                ErrorCode.UNKNOWN_ERROR.getCode(),
                ErrorCode.UNKNOWN_ERROR.getMessage()
        );
    }
}
