package g3.hydrantmana.common.context;

public enum ResultCode {
    SUCCESS(0, "成功"),
    ERROR(1000, "未知错误"),
    VALIDATION_FAILED(1001, "参数校验失败"),
    UNAUTHORIZED(2001, "未授权访问"),
    FORBIDDEN(2002, "禁止访问");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
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
