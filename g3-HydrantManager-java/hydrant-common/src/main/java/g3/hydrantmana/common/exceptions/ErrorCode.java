package g3.hydrantmana.common.exceptions;

public enum ErrorCode {
    SUCCESS(0, "成功"),
    UNKNOWN_ERROR(1000, "未知错误"),
    VALIDATION_FAILED(1001, "参数校验失败"),
    USER_NOT_FOUND(2001, "用户不存在"),
    PERMISSION_DENIED(2002, "没有权限"),
    DATABASE_ERROR(3001, "数据库操作失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
