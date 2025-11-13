package g3.hydrantmana.hydrantweb.service.impl;

import g3.hydrantmana.domain.dto.LoginDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.hydrantweb.mapper.LoginMapper;
import g3.hydrantmana.hydrantweb.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Resource
    LoginMapper loginMapper;

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        User user = loginMapper.selectByUsername(username);
        // 校验用户名
        if (user == null){
            try {
                throw new Exception("用户名不存在!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // 校验密码
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5.equals(user.getPassword())){
            try {
                throw new Exception("密码错误!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 校验账号状态
        if (user.getStatus() == 0){
            try {
                throw new Exception("账号被锁定");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return user;
    }
}
