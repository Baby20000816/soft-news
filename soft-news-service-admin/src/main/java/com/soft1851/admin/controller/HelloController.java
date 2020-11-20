package com.soft1851.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class HelloController {
    public static void main(String[] args) {
        String pwd = BCrypt.hashpw("123123", BCrypt.gensalt());
        System.out.println(pwd);
    }
}
