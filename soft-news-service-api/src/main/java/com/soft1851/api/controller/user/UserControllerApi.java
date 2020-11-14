package com.soft1851.api.controller.user;

import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "用户信息相关Controller",tags = {"用户信息相关Controller"})
@RequestMapping("user")
public interface UserControllerApi {

    /**
     * 获取所有用户
     * @return users: 所有用户信息
     */
    @ApiOperation(value = "获取所有用户信息",notes = "获取所有用户信息",httpMethod = "POST")
    @GetMapping("/users")
    GraceResult getAllUsers();

    @ApiOperation(value="获得用户基本信息",notes = "获得用户基本信息",httpMethod = "POST")
    @PostMapping("/userInfo")
    GraceResult getUserInfo(@RequestParam String userId);
}