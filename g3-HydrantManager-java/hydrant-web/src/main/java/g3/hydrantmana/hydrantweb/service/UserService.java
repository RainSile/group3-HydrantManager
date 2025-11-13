package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.UserDTO;
import g3.hydrantmana.domain.query.UserQuery;

public interface UserService {

    /**
     * 查询用户(分页+条件)
     * @param userQuery
     * @return
     */
    PageDTO<UserDTO> listUser(UserQuery userQuery);

    /**
     * 添加用户
     * @param userDTO
     */
    void addUser(UserDTO userDTO);

    /**
     * 修改用户状态
     * @param stat
     * @param id
     */
    void changeStatus(Integer stat,String id);

    /**
     * 修改密码
     * @param pwd
     * @param id
     */
    void changePassword(String pwd);
}
