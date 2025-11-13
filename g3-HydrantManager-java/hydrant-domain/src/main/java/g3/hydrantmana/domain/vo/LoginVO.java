package g3.hydrantmana.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "LoginVO" ,description = "登录数据返回对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {

    @Schema(description = "主键值",example = "1757482000001233920")
    private String id;

    @Schema(description = "用户名",example = "zhangsan")
    private String username;

    @Schema(description = "jwt令牌", example =
            "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicm9sZSI6ImFkbWluIiwidXNlcm5hbWUiOiJzdXBlckFkbWluIiwiZXhwIjoxNzI4ODI3MDU1fQ.js2dj3Q0ztwR7Z3Qo7Gv1Bfe8LFHnau_8J7dID4IhHk\n")
    private String token;
}
