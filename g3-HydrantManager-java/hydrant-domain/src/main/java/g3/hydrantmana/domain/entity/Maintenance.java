package g3.hydrantmana.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("maintenance_mapping")
public class Maintenance implements Serializable {

    /** 主键ID（雪花算法生成） */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 维修责任人id */
    private String uid;

    /** 消防栓id */
    private String hid;

    /** 逻辑删除字段 */
    private Integer deleted;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
