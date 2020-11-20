package com.soft1851.admin.service;

import com.soft1851.pojo.AdminUser;

public interface AdminUserService {
    AdminUser queryAdminByUsername(String username);
}
