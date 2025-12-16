package g3.hydrantmana.hydrantweb;

import g3.hydrantmana.common.properties.JwtProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("g3.hydrantmana.hydrantweb.mapper")
@EnableConfigurationProperties(JwtProperties.class)
public class HydrantWebApplication {
    //feat:text
    public static void main(String[] args) {
        SpringApplication.run(HydrantWebApplication.class, args);
    }

}
