package g3.hydrantmana.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public class FireAlarmVo {
    @Min(value = 1,message = "最小查询页码数为1")
    @Schema(description = "页码", example = "1",required = true)
    private Integer pageIndex = 1;

    @Min(value = 1,message = "最小查询条数为1")
    @Schema(description = "每页条数", example = "10",required = true)
    private Integer pageSize = 10;
}
