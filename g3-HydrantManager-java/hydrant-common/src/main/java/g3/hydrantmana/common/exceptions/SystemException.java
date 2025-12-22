package g3.hydrantmana.common.exceptions;

public class SystemException extends BaseException {

    public SystemException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SystemException(String message) {
        super(ErrorCode.UNKNOWN_ERROR.getCode(), message);
    }
}
