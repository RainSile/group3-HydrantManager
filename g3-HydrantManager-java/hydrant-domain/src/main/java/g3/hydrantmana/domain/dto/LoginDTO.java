package g3.hydrantmana.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "LoginDTO",description = "登录数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {

    @Schema(description = "用户名",example = "zhangsan")
    private String username;

    @Schema(description = "密码",example = "123456")
    private String password;
}
