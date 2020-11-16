package com.soft1851.user.service;

import com.soft1851.pojo.AppUser;

public interface UserService {
    AppUser queryMobileIsExist(String mobile);

    AppUser createUser(String mobile);
}
