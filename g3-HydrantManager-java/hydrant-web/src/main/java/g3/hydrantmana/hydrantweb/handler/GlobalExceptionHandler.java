package g3.hydrantmana.hydrantweb.handler;

import g3.hydrantmana.domain.vo.JsonVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public JsonVO exceptionHandler(Exception ex){
        return JsonVO.fail(ex.getMessage());
    }
}
