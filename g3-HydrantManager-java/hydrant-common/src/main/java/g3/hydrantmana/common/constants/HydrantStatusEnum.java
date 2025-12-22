package g3.hydrantmana.common.constants;

public enum HydrantStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(0, "离线"),
    MAINTENANCE(2, "维护中");

    private final int code;
    private final String message;

    HydrantStatusEnum(int code, String message) {
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
