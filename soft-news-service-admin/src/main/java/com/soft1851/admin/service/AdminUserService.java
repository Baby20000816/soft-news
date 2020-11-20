package com.soft1851.admin.service;

import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.NewAdminBO;
import com.soft1851.utils.PageGridResult;
import io.swagger.models.auth.In;

public interface AdminUserService {
    AdminUser queryAdminByUsername(String username);

    void createAdminUser(NewAdminBO newAdminBO);

    PageGridResult queryAdminList(Integer page, Integer pageSize);
}
