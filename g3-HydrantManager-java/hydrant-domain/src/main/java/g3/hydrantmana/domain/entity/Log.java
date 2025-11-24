package g3.hydrantmana.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 维修日志表实体类
 */
@Getter
@Setter
@TableName("maintenance_log")
public class Log implements Serializable {

    /** 主键ID(雪花算法生成) **/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 维修表ID **/
    private String mid;

    /** 维修记录 **/
    private String record;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
