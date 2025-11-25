package g3.hydrantmana.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "MaintenanceDTO",description = "维修关系映射数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "维修关系映射表id",example = "8985319842072588288",accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "维修责任人id",example = "1757482000107992064")
    private String uid;

    @Schema(description = "故障消防栓id",example = "1757481893034569728")
    private String hid;
}