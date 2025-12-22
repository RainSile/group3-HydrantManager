package g3.hydrantmana.common.exceptions;

public class BusinessException extends BaseException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(String message) {
        super(ErrorCode.UNKNOWN_ERROR.getCode(), message);
    }
}
