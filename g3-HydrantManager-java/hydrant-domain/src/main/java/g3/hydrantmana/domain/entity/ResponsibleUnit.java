package g3.hydrantmana.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResponsibleUnit implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField(value = "unit_name")
    private String unitName;

    @TableField(value = "unit_type")
    private Integer unitType;

    @TableField(value = "contact_person")
    private String contactPerson;

    @TableField(value = "contact_phone")
    private String contactPhone;

    private String address;

    @TableField(value = "managed_hydrant")
    private List<Hydrant> managedHydrant;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
