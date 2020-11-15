package com.soft1851.api.controller;

import org.springframework.web.bind.annotation.GetMapping;

public interface HelloControllerApi {

    @GetMapping("/hello")
    Object hello();
}
