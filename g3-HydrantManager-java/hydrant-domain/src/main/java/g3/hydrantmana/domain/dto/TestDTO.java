package g3.hydrantmana.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "TestDTO", description = "测试数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO implements Serializable {

    @Schema(description = "测试字段", example = "Hello")
    private String testField;
}
