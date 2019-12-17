package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.vo.ResponseVO;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @ApiOperation(value = "检查token是否过期")
    @GetMapping("/token/{token}")
    public ResponseVO checkToken(@PathVariable String token) {
        return userService.checkToken(token);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseVO wxLogin(@RequestParam String code) {
        return userService.login(code);
    }

    @ApiOperation("修改用户信息")
    @PostMapping("/info")
    public ResponseVO updateUserInfo(@RequestHeader("Authorization") String token, User user) {
        return userService.updateUser(token, user);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public ResponseVO selectInfo(@RequestHeader("Authorization") String token) {
        //暂未写获取已登录用户信息
        return userService.getUserInfo(token);
    }

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public ResponseVO register(User user, String code) {
        return userService.register(user, code);
    }

}
