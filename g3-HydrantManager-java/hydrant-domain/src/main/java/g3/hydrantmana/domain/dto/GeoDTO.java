package g3.hydrantmana.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 地理信息数据传输对象
 */
@Schema(name = "GeoDTO", description = "地理信息数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoDTO implements Serializable {

    // 消防栓id
    private String hid;

    // 经度
    private BigDecimal latitude;

    // 纬度
    private BigDecimal longitude;

    // 距离
    private Double distance;
}
