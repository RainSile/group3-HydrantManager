package g3.hydrantmana.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import g3.hydrantmana.domain.entity.Hydrant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleUnitDTO implements Serializable {

    private String id;

    private String unitName;

    private Integer unitType;

    private String contactPerson;

    private String contactPhone;

    private String address;

    private List<Hydrant> managedHydrant;

    private LocalDateTime updateTime;
}
