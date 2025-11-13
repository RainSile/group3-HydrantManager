package g3.hydrantmana.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 消防栓信息数据传输对象
 */
@Schema(name = "HydrantDTO", description = "消防栓信息数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HydrantDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键id",example = "1988966178818719745",accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "详细位置描述", example = "中山市石岐街道莲塘路口东侧消防栓")
    private String location;

    @Schema(description = "经度", example = "113.382061")
    private BigDecimal longitude;

    @Schema(description = "纬度", example = "22.517893")
    private BigDecimal latitude;

    @Schema(description = "状态：0-正常，1-维护中，2-故障", example = "0")
    private Integer status;

    @Schema(description = "当前水压 (MPa)", example = "0.62")
    private BigDecimal pressure;

    @Schema(description = "当前流速 (L/S)", example = "1.25")
    private BigDecimal flowRate;

    @Schema(description = "创建时间", example = "2025-11-09T09:30:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2025-11-09T12:00:00")
    private LocalDateTime updateTime;

    @Schema(description = "备注信息", example = "例行巡检正常，无漏水现象")
    private String data;
}
