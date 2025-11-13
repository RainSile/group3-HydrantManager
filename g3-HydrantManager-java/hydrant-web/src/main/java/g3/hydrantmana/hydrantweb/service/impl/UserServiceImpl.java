package g3.hydrantmana.hydrantweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.context.BaseContext;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.UserDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.domain.query.UserQuery;
import g3.hydrantmana.hydrantweb.mapper.UserMapper;
import g3.hydrantmana.hydrantweb.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    /**
     * 查询用户(分页+条件)
     * @param query
     * @return
     */
    @Override
    public PageDTO<UserDTO> listUser(UserQuery query) {
        Page<UserDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        userMapper.selectUser(page,query);
        return PageDTO.of(page);
    }

    /**
     * 添加用户
     * @param userDTO
     */
    @Override
    public void addUser(UserDTO userDTO) {
        User user = new User();

        // md5加密
        String oriPwd = userDTO.getPassword();
        String md5Pwd =
                DigestUtils.md5DigestAsHex(oriPwd.getBytes());
        user.setPassword(md5Pwd);

        BeanUtils.copyProperties(userDTO, user, "password");
        userMapper.insert(user);
    }

    /**
     * 修改账号状态
     * @param stat
     * @param id
     */
    @Override
    public void changeStatus(Integer stat, String id) {
        int affectedRows = userMapper.updateStatus(stat, id);
        if (affectedRows == 0){
            try {
                throw new Exception("没有内容被更新,数据不存在!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 修改密码
     * @param pwd
     * @param id
     */
    @Override
    public void changePassword(String pwd) {
        String md5pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        Long id = BaseContext.getCurrentId();
        String userid = String.valueOf(id);
        int affectedRows = userMapper.updatePassword(md5pwd, userid);
        if (affectedRows == 0){
            try {
                throw new Exception("没有内容被更新,数据不存在!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
