package g3.hydrantmana.hydrantweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.domain.dto.UserDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.domain.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户(条件+分页)
     * @param page
     * @param userQuery
     * @return
     */
    Page<UserDTO> selectUser(Page<UserDTO> page,
                    @Param("query")UserQuery userQuery);

    /**
     * 更新账号状态
     * @param id
     * @param status
     * @return
     */
    @Update("UPDATE user_table SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("status") Integer status,@Param("id") String id);

    /**
     * 更新密码
     * @param id
     * @param pwd
     * @return
     */
    @Update("UPDATE user_table SET password = #{pwd} WHERE id = #{id}")
    int updatePassword(@Param("pwd") String pwd,@Param("id") String id);

}
