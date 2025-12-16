package g3.hydrantmana.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;

public class FireAlarm {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 详细位置描述 */
    private String location;

    /** 经度 */
    private BigDecimal longitude;
}
