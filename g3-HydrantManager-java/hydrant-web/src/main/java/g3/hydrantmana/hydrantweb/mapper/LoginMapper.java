package g3.hydrantmana.hydrantweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import g3.hydrantmana.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper extends BaseMapper<User> {

    @Select("select * from user_table where username=#{username}")
    User selectByUsername(String username);
}
