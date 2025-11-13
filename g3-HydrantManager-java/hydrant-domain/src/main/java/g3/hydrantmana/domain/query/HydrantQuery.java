package g3.hydrantmana.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(name = "HydrantQuery", description = "消防栓信息查询对象")
@Data
public class HydrantQuery extends PageQuery implements Serializable {

    @Schema(description = "消防栓位置关键字（模糊查询）", example = "石岐街道")
    private String location;

    @Schema(description = "状态（0: 正常，1: 维护中，2: 故障）", example = "0")
    private Integer status;

    @Schema(description = "最小水压 (MPa)", example = "0.4")
    private Double minPressure;

    @Schema(description = "最大水压 (MPa)", example = "1.2")
    private Double maxPressure;

    @Schema(description = "最小流速 (L/S)", example = "0.5")
    private Double minFlowRate;

    @Schema(description = "最大流速 (L/S)", example = "2.0")
    private Double maxFlowRate;

    @Schema(description = "按更新时间范围查询 - 起始时间", example = "2025-11-01 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "按更新时间范围查询 - 结束时间", example = "2025-11-09 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
