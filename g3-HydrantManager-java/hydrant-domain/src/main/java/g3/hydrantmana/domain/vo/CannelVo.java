package g3.hydrantmana.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class CannelVo {
    @Schema(description = "维修关系映射表id",example = "8985319842072588288")
    private String mid;

    /** 维修记录 **/
    @Schema(description = "日志记录",example = "第一次检修:经过数小时问题排查,发现这个消防栓根本不存在")
    private String record;

    /** 更新时间 */
    @Schema(description = "更新时间",example = "2025-11-23 09:35:00")
    private LocalDateTime updateTime;
}
