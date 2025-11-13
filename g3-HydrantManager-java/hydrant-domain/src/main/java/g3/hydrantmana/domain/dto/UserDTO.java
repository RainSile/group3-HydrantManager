package g3.hydrantmana.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息数据传输对象
 */
@Schema(name = "UserDTO",description = "用户信息数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "主键id",example = "1988966178818719745",accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "用户名",example = "zhangsan")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码",example = "123456",accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Schema(description = "联系电话",example = "13824750011")
    private String phone;

    @Schema(description = "权限(0:普通用户,1:管理员)",example = "1")
    private Integer priv;

    @Schema(description = "账号状态(0:禁用,1:启用)",example = "1")
    private Integer status;
}
