package com.soft1851.user.controller;

import com.soft1851.api.controller.HelloControllerApi;
import com.soft1851.result.GraceResult;
import com.soft1851.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    @Autowired
    private RedisOperator redis;
    @Override
    public Object hello() {
        return GraceResult.ok("hello");
    }

    @GetMapping("/redis")
    public GraceResult redis(){
        redis.set("age","20");
        return GraceResult.ok(redis.get("age"));
    }
}
