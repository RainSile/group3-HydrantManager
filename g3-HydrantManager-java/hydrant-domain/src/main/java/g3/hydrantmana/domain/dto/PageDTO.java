package g3.hydrantmana.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 通用分页返回对象
 * 用于将 MyBatis-Plus Page 对象转换为前端友好的分页数据结构
 */
@Schema(name = "PageDTO", description = "通用分页数据传输对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> implements Serializable {

    @Schema(description = "当前页码", example = "1")
    private Long pageIndex;

    @Schema(description = "每页显示条数", example = "10")
    private Long pageSize;

    @Schema(description = "总条数", example = "500")
    private Long total;

    @Schema(description = "总页数", example = "50")
    private Long pages;

    @Schema(description = "当前页数据列表")
    private List<T> records = new ArrayList<>();

    /**
     * 将 MyBatis-Plus 的 Page<T> 直接转换为 PageDTO<T>
     */
    public static <T> PageDTO<T> of(Page<T> page) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setPageIndex(page.getCurrent());
        dto.setPageSize(page.getSize());
        dto.setTotal(page.getTotal());
        dto.setPages(page.getPages());
        dto.setRecords(page.getRecords());
        return dto;
    }

    /**
     * 将 Page<D> 转换为 PageDTO<T>，通过转换函数进行数据映射
     *
     * @param page       原始分页数据
     * @param converter  数据转换函数，例如 entity -> dto
     */
    public static <T, D> PageDTO<T> of(Page<D> page, Function<D, T> converter) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setPageIndex(page.getCurrent());
        dto.setPageSize(page.getSize());
        dto.setTotal(page.getTotal());
        dto.setPages(page.getPages());

        if (page.getRecords() != null) {
            List<T> list = new ArrayList<>();
            for (D record : page.getRecords()) {
                list.add(converter.apply(record));
            }
            dto.setRecords(list);
        }
        return dto;
    }

    /**
     * 默认转换方式：将 D 的属性复制到 T（同名字段自动匹配）
     *
     * @param page   原始分页数据
     * @param tClass 目标类类型
     */
    public static <T, D> PageDTO<T> of(Page<D> page, Class<T> tClass) {
        return of(page, source -> {
            try {
                T target = tClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
                return target;
            } catch (Exception e) {
                throw new RuntimeException("分页数据转换失败: " + e.getMessage(), e);
            }
        });
    }
}
