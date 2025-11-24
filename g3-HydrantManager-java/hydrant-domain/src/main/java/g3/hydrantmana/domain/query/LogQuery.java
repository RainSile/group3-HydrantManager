package g3.hydrantmana.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(name = "LogQuery", description = "日志信息查询对象")
@Data
public class LogQuery extends PageQuery implements Serializable {

    @Schema(description = "维修关系映射表id", example = "8985319842072588288")
    private String mid;

    @Schema(description = "按更新时间范围查询 - 起始时间", example = "2025-11-23 09:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "按更新时间范围查询 - 结束时间", example = "2025-11-23 10:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
