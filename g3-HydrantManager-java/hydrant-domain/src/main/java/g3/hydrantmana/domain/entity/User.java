package g3.hydrantmana.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息表实体类
 */
@Getter
@Setter
@TableName("user_table")
public class User implements Serializable {

    /** 主键ID（雪花算法生成） */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 用户名 */
    private String username;

    /** 密码（已加密存储） */
    private String password;

    /** 联系电话 */
    private String phone;

    /** 权限：0-普通用户，1-管理员/维修人员 */
    private Integer priv;

    /** 账号状态：0-禁用，1-启用 */
    private Integer status;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间（插入时与创建时间一致） */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
