package com.soft1851.api.controller.user;

import com.soft1851.pojo.Fans;
import com.soft1851.pojo.bo.UpdateUserInfoBO;
import com.soft1851.result.GraceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "用户信息相关Controller",tags = {"用户信息相关Controller"})
@RequestMapping("user")
public interface UserControllerApi {
    /**
     * 获取所有用户
     * @return users: 所有用户信息
     */
    @ApiOperation(value = "获取所有用户信息",notes = "获取所有用户信息",httpMethod = "GET")
    @GetMapping("/users")
    GraceResult getAllUsers();

    @ApiOperation(value="获得用户基本信息",notes = "获得用户基本信息",httpMethod = "POST")
    @PostMapping("/userInfo")
    GraceResult getUserInfo(@RequestParam String userId);

    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "完善用户信息",notes = "完善用户信息",httpMethod = "POST")
    GraceResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO, BindingResult result);

    @PostMapping("/userBaseInfo")
    @ApiOperation(value = "获得用户基础信息",notes = "获得用户基础信息",httpMethod = "POST")
    GraceResult getUserBasicInfo(@RequestParam String userId);

    @PostMapping("/fans/follow")
    @ApiOperation(value = "获得粉丝信息",notes = "获得粉丝信息",httpMethod = "POST")
    GraceResult getFansFollow(@RequestParam String writerId);

    @ApiOperation(value = "根据用户的ids查询用户列表",notes = "根据用户的ids查询用户列表",httpMethod = "GET")
    @GetMapping("/queryByIds")
    GraceResult queryByIds(@RequestParam String userIds);
}