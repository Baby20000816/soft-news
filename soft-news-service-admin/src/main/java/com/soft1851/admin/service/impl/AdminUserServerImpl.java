package com.soft1851.admin.service.impl;

import com.soft1851.admin.mapper.AdminUserMapper;
import com.soft1851.admin.service.AdminUserService;
import com.soft1851.exception.GraceException;
import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.NewAdminBO;
import com.soft1851.result.ResponseStatusEnum;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminUserServerImpl implements AdminUserService {
    public final AdminUserMapper adminUserMapper;
    public final Sid sid;

    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminUserExample = new Example(AdminUser.class);
        Example.Criteria adminUserCriteria = adminUserExample.createCriteria();
        adminUserCriteria.andEqualTo("username",username);
        return adminUserMapper.selectOneByExample(adminUserExample);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        if (StringUtils.isNotBlank(newAdminBO.getPassword())){
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(),BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())){
            adminUser.setFaceId(newAdminBO.getFaceId());
        }
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());
        int result = adminUserMapper.insert(adminUser);
        if (result!=1){
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }
}
