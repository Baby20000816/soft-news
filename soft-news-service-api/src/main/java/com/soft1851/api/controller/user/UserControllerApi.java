package com.soft1851.api.controller.user;

import org.springframework.web.bind.annotation.GetMapping;

public interface UserControllerApi {

    /**
     * 获取所有用户
     * @return users: 所有用户信息
     */
    @GetMapping("/users")
    Object getUsers();
}