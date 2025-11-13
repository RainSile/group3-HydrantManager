package g3.hydrantmana.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties("hydrant.jwt")
@Component
public class JwtProperties {
    /**
     * 生成jwt令牌相关配置
     */
    private String secretKey;
    private long ttl;
    private String tokenName;
}
