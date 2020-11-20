package com.soft1851.admin.controller;

import com.soft1851.admin.service.AdminUserService;
import com.soft1851.api.BaseController;
import com.soft1851.api.controller.admin.AdminMsgControllerApi;
import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.AdminLoginBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AdminMsgController extends BaseController implements AdminMsgControllerApi {
    @Autowired
    private AdminUserService adminUserService;
    @Override
    public GraceResult adminLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        if (admin==null){
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(),admin.getPassword());
        if (isPwdMatch){
            doLoginSettings(admin,request,response);
            return GraceResult.ok();
        }
        else {
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    private void doLoginSettings(AdminUser admin, HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN+":"+admin.getId(),token);
        setCookie(request,response,"aToken",token,COOKIE_MONTH);
        setCookie(request,response,"aId",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aName",admin.getAdminName(),COOKIE_MONTH);
    }
}
