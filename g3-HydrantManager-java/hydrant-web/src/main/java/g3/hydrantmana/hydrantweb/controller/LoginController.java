package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.common.context.BaseContext;
import g3.hydrantmana.domain.dto.LoginDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.domain.vo.LoginVO;
import g3.hydrantmana.hydrantweb.service.LoginService;
import g3.hydrantmana.common.properties.JwtProperties;
import g3.hydrantmana.common.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "登录登出")
@RestController
@Slf4j
public class LoginController {
    @Resource
    LoginService loginService;
    @Resource
    JwtProperties jwtProperties;

    @Operation(tags = "登录登出",summary = "登录")
    @PostMapping("/login")
    public JsonVO<LoginVO> login(@RequestBody LoginDTO loginDTO){
        User user = loginService.login(loginDTO);
        // 登陆成功后，生成jwt令牌
        Map<String,Object> claims = new HashMap<>();
        claims.put("userid",user.getId());
        claims.put("role",user.getPriv()==0 ? "user" : "admin");
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
        // 返回数据
        LoginVO loginVO = new LoginVO(user.getId(), user.getUsername(), token);
        return JsonVO.success(loginVO);
    }

    @Operation(tags = "登录登出",summary = "登出")
    @PostMapping("/logout")
    public JsonVO logout(){
        BaseContext.removeCurrentId();
        return JsonVO.success("登出成功");
    }
}
