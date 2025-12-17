package g3.hydrantmana.domain.dto;

import lombok.Data;

@Data
public class HydrantReportDTO {
    private String location;
    private double pressure;
    private double flowRate;

    // 模拟生成状态报告的方法
    public String generateReport() {
        return String.format("Hydrant at %s: pressure %.2f MPa, flow %.2f L/S", location, pressure, flowRate);
    }
}
