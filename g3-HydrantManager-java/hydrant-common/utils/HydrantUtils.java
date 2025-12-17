package g3.hydrantmana.common.utils;

import java.math.BigDecimal;

public class HydrantUtils {

    // 计算两点之间的简单欧氏距离
    public static double calculateDistance(BigDecimal lat1, BigDecimal lon1,
                                           BigDecimal lat2, BigDecimal lon2) {
        double dx = lat1.doubleValue() - lat2.doubleValue();
        double dy = lon1.doubleValue() - lon2.doubleValue();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 判断水压状态：正常、偏低、偏高
    public static String pressureStatus(BigDecimal pressure) {
        if (pressure == null) return "未知";
        double p = pressure.doubleValue();
        if (p < 0.5) return "偏低";
        else if (p > 1.5) return "偏高";
        else return "正常";
    }
}
