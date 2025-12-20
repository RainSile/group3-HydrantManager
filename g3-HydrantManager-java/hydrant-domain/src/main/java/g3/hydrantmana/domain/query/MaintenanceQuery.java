package g3.hydrantmana.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Schema(name = "MaintenanceQuery", description = "维修关系映射信息查询对象")
@Data
public class MaintenanceQuery extends PageQuery implements Serializable {

    @Schema(description = "维修责任人id", example = "1757482000107992064")
    private String uid;

    @Schema(description = "故障消防栓id", example = "1757481893034569728") 
    private String hid;
}