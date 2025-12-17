package g3.hydrantmana.common.utils;

import java.math.BigDecimal;

public class HydrantUtils {

    /**
     * 计算两点之间的欧几里得距离（单位：米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dx = lat1 - lat2;
        double dy = lon1 - lon2;
        return Math.sqrt(dx * dx + dy * dy) * 111000; // 近似换算为米
    }

    /**
     * 根据水压值判断消防栓状态
     * @param pressure 水压 (MPa)
     * @return 状态字符串
     */
    public static String getPressureStatus(BigDecimal pressure) {
        if (pressure == null) {
            return "未知";
        }
        double p = pressure.doubleValue();
        if (p >= 1.5) {
            return "正常";
        } else if (p >= 0.8) {
            return "警告";
        } else {
            return "故障";
        }
    }

    /**
     * 生成消防栓状态报告字符串
     */
    public static String generateReport(String location, BigDecimal pressure, BigDecimal flowRate) {
        return String.format("消防栓位置: %s, 水压: %.2f MPa, 流速: %.2f L/S, 状态: %s",
                location,
                pressure != null ? pressure.doubleValue() : 0.0,
                flowRate != null ? flowRate.doubleValue() : 0.0,
                getPressureStatus(pressure));
    }

    // 测试 main 方法 (仅用于演示，不影响程序)
    public static void main(String[] args) {
        System.out.println(generateReport("中心广场", new BigDecimal("1.2"), new BigDecimal("15.5")));
    }
}
