package g3.hydrantmana.hydrantweb.interceptor;

import g3.hydrantmana.common.context.BaseContext;
import g3.hydrantmana.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import g3.hydrantmana.common.properties.JwtProperties;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getTokenName());
        //   从请求中获取uri
        String uri = request.getRequestURI();
        log.info("当前请求URI:{}",uri);

        //2、校验令牌
        try {
            log.info("jwt校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token, uri);
            String role = claims.get("role", String.class);

            // 将userid放入threadlocal中以便于后续service调用
            String userid = claims.get("userid", String.class);
            long id = Long.parseLong(userid);
            BaseContext.setCurrentId(id);

            // 普通用户权限控制
            if ("user".equals(role)) {

                // hydrant模块
                if (uri.startsWith("/hydrant")) {
                    if (!(uri.startsWith("/hydrant/query"))) {
                        log.warn("普通用户禁止访问: {}", uri);
                        response.setStatus(401);
                        return false;
                    }
                }

                // user模块
                if (uri.startsWith("/user")) {
                    // 允许 user/query、user/password
                    if (!(uri.startsWith("/user/query") ||
                            uri.startsWith("/user/password"))) {
                        log.warn("普通用户禁止访问: {}", uri);
                        response.setStatus(401);
                        return false;
                    }
                }

                // maintenance模块
                if (uri.startsWith("/maintenance")) {
                    // 允许 /maintenance/query
                    if (!(uri.startsWith("/maintenance/query"))) {
                        log.warn("普通用户禁止访问: {}", uri);
                        response.setStatus(401);
                        return false;
                    }
                }

                // maintenance-log
                if (uri.startsWith("/log")) {
                    // 允许 /log/query
                    if (!(uri.startsWith("/log/query"))) {
                        log.warn("普通用户禁止访问: {}", uri);
                        response.setStatus(401);
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            response.setStatus(401);
            return false;
        }

    }
}