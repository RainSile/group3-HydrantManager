package g3.hydrantmana.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "GeoMinDistanceQuery", description = "地理上最短路径查询对象")
@Data
public class GeoMinDistanceQuery implements Serializable {

    private BigDecimal longitude;

    private BigDecimal latitude;
}
