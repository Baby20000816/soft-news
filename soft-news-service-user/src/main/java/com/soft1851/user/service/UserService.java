package com.soft1851.user.service;

import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.bo.UpdateUserInfoBO;
import com.soft1851.result.GraceResult;

public interface UserService {
    AppUser queryMobileIsExist(String mobile);

    AppUser createUser(String mobile);

    public AppUser getUser(String userId);

    void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
//    public GraceResult getUserInfo(String userId);
}
