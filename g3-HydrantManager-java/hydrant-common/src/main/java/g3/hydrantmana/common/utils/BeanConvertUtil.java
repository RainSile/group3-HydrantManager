package g3.hydrantmana.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 通用对象转换工具
 * 用于 DTO / Entity / VO 之间的属性拷贝
 */
public class BeanConvertUtil {

    /**
     * 单个对象转换
     *
     * @param source 原对象
     * @param targetSupplier 目标对象构造器
     * @param <S> source 类型
     * @param <T> target 类型
     * @return 转换后的对象
     */
    public static <S, T> T convert(S source, Supplier<T> targetSupplier) {
        if (source == null) {
            return null;
        }
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * 列表对象转换
     *
     * @param sourceList 原对象列表
     * @param targetSupplier 目标对象构造器
     * @param <S> source 类型
     * @param <T> target 类型
     * @return 转换后的对象列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Supplier<T> targetSupplier) {
        List<T> result = new ArrayList<>();
        if (sourceList == null || sourceList.isEmpty()) {
            return result;
        }
        for (S source : sourceList) {
            result.add(convert(source, targetSupplier));
        }
        return result;
    }
}
