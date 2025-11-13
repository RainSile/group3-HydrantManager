package g3.hydrantmana.hydrantweb.controller;

import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.UserDTO;
import g3.hydrantmana.domain.entity.User;
import g3.hydrantmana.domain.query.UserQuery;
import g3.hydrantmana.domain.vo.JsonVO;
import g3.hydrantmana.hydrantweb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Operation(tags = "用户管理",summary = "查询用户(条件+分页)")
    @GetMapping("/query")
    public JsonVO<PageDTO<UserDTO>> listUser(UserQuery userQuery){
        PageDTO<UserDTO> pageDTO = userService.listUser(userQuery);
        return JsonVO.success(pageDTO);
    }

    @Operation(tags = "用户管理",summary = "添加用户")
    @PostMapping("/add")
    public JsonVO addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return JsonVO.success("创建用户成功!");
    }

    @Operation(tags = "用户管理",summary = "账号状态管理")
    @PutMapping("/status/{stat}")
    public JsonVO statusUser(@PathVariable Integer stat,String id){
        userService.changeStatus(stat,id);
        return JsonVO.success("账号状态修改成功!");
    }

    @Operation(tags = "用户管理",summary = "修改密码")
    @PutMapping("/password")
    public JsonVO statusUser(String pwd){
        userService.changePassword(pwd);
        return JsonVO.success("密码修改成功!");
    }
}
