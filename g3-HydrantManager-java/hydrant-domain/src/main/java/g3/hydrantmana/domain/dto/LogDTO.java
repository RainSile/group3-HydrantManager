package g3.hydrantmana.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "LogDTO",description = "维修日志数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO implements Serializable {

    @Schema(description = "维修关系映射表id",example = "8985319842072588288")
    private String mid;

    @Schema(description = "日志记录",example = "第一次检修:经过数小时问题排查,发现这个消防栓根本不存在")
    private String record;
}
