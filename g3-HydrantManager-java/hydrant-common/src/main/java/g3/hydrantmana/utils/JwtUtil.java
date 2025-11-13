package g3.hydrantmana.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成 JWT
     *
     * @param secretKey 秘钥（来自 hydrant.jwt.secretKey）
     * @param ttlMillis 过期时间毫秒（来自 hydrant.jwt.ttl）
     * @param claims    自定义载荷信息（如 userId、role）
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT，并根据当前请求路径进行权限控制
     *
     * @param secretKey 秘钥
     * @param token     JWT Token
     * @param uri       当前请求路径（如 "/hydrant/list"）
     * @return Claims   载荷对象
     */
    public static Claims parseJWT(String secretKey, String token, String uri) {

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String role = claims.get("role", String.class);

        // 普通用户禁止所有 /hydrant/** 接口
        if ("user".equals(role) && uri.startsWith("/hydrant")) {
            throw new RuntimeException("普通用户无权访问该接口");
        }

        return claims;
    }

}
