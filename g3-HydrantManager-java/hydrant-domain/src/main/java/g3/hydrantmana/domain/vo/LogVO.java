package g3.hydrantmana.domain.vo;

import g3.hydrantmana.domain.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(name = "LogVO" ,description = "维修日志数据返回对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogVO implements Serializable {
    /** 主键ID(雪花算法生成) **/
    @Schema(description = "日志id",example = "8985319842099277824")
    private String id;

    /** 维修表ID **/
    @Schema(description = "维修关系映射表id",example = "8985319842072588288")
    private String mid;

    /** 维修记录 **/
    @Schema(description = "日志记录",example = "第一次检修:经过数小时问题排查,发现这个消防栓根本不存在")
    private String record;

    /** 更新时间 */
    @Schema(description = "更新时间",example = "2025-11-23 09:35:00")
    private LocalDateTime updateTime;
}
