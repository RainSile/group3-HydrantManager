package g3.hydrantmana.domain.dto;

public class FireAlarm {

    /** 主键 */
    private String id;

    /** 报警设备编号 */
    private String alarmCode;

    /** 报警类型（SMOKE / TEMP / GAS） */
    private String alarmType;

    /** 报警等级（1-低 2-中 3-高） */
    private String alarmLevel;

    /** 当前状态（NORMAL / ALARM / OFFLINE） */
    private String status;

    /** 所属区域ID */
    private String regionId;
}
