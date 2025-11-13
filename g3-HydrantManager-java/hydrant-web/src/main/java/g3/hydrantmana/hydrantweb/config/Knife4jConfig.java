package g3.hydrantmana.hydrantweb.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableKnife4j
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
                new Info()
                        .title("消防栓管理系统接口文档")
                        .version("0.0.1")
                        .description("使用OpenAPI3 + Knife4j生成的消防栓管理系统接口文档")
                        .contact(new Contact()
                                .name("rainsilent")
                                .email("rainsilent233@gmail.com")
                        )
        );
    }
}
