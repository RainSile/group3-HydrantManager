package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.LoginDTO;
import g3.hydrantmana.domain.entity.User;

public interface LoginService {

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    User login(LoginDTO loginDTO);
}
