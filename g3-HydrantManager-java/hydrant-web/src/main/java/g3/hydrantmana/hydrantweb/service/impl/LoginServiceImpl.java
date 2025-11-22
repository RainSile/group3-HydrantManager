package g3.hydrantmana.hydrantweb.service.impl;

import g3.hydrantmana.common.constants.UserConstants;
import g3.hydrantmana.common.exceptions.PasswordIncorrectException;
import g3.hydrantmana.common.exceptions.UserLockedException;
import g3.hydrantmana.common.exceptions.UserNotExistException;
import g3.hydrantmana.domain.dto.LoginDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.hydrantweb.mapper.LoginMapper;
import g3.hydrantmana.hydrantweb.service.LoginService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.security.auth.login.AccountLockedException;

import static g3.hydrantmana.common.constants.UserConstants.Status;

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
            throw new UserNotExistException("登陆失败,用户不存在");
        }
        // 校验密码
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5.equals(user.getPassword())){
            throw new PasswordIncorrectException("登录失败,密码错误");
        }

        // 校验账号状态
        if (user.getStatus() == Status.LOCKED){
            throw new UserLockedException("无法登录,目标用户账户被禁用");
        }

        return user;
    }
}
