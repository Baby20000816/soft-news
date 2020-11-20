package com.soft1851.admin.service;

import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.NewAdminBO;

public interface AdminUserService {
    AdminUser queryAdminByUsername(String username);

    void createAdminUser(NewAdminBO newAdminBO);
}
