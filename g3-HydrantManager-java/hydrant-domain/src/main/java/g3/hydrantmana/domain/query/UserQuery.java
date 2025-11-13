package g3.hydrantmana.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name= "UserQuery" ,description = "用户信息查询对象")
public class UserQuery extends PageQuery implements Serializable {

    @Schema(description = "用户名",example = "zhangsan")
    private String username;

    @Schema(description = "联系电话",example = "13824750011")
    private String phone;

    @Schema(description = "权限(0:普通用户,1:管理员)",example = "1")
    private Integer priv;

    @Schema(description = "账号状态(0:禁用,1:启用)",example = "1")
    private Integer status;
}
