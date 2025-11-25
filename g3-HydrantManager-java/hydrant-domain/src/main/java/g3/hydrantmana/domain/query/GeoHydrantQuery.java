package g3.hydrantmana.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(name = "GeoHydrantQuery", description = "地理消防栓查询对象")
@Data
public class GeoHydrantQuery implements Serializable {

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Double distance;
}
