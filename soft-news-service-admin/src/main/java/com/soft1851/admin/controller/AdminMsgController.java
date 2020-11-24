package com.soft1851.admin.controller;

import com.soft1851.admin.service.AdminUserService;
import com.soft1851.api.BaseController;
import com.soft1851.api.controller.admin.AdminMsgControllerApi;
import com.soft1851.enums.FaceVerifyType;
import com.soft1851.exception.GraceException;
import com.soft1851.pojo.AdminUser;
import com.soft1851.pojo.bo.AdminLoginBO;
import com.soft1851.pojo.bo.NewAdminBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.utils.FaceVerifyUtil;
import com.soft1851.utils.PageGridResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminMsgController extends BaseController implements AdminMsgControllerApi {
    private final AdminUserService adminUserService;
    private final RestTemplate restTemplate;
    private final FaceVerifyUtil faceVerifyUtil;

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

    @Override
    public GraceResult addNewAdmin(HttpServletRequest request, HttpServletResponse response, NewAdminBO newAdminBO) {
        if (StringUtils.isBlank(newAdminBO.getImg64())){
            if (StringUtils.isBlank(newAdminBO.getPassword())||StringUtils.isBlank(newAdminBO.getConfirmPassword())){
                return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }

        if (StringUtils.isNotBlank(newAdminBO.getPassword())){
            if (!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())){
                return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        checkAdminExist(newAdminBO.getUsername());
        adminUserService.createAdminUser(newAdminBO);
        return GraceResult.ok();
    }

    @Override
    public GraceResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceResult.ok();
    }

    private void checkAdminExist(String username) {
        AdminUser admin = adminUserService.queryAdminByUsername(username);
        if (admin!=null){
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    private void doLoginSettings(AdminUser admin, HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN+":"+admin.getId(),token);
        setCookie(request,response,"aToken",token,COOKIE_MONTH);
        setCookie(request,response,"aId",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aName",admin.getAdminName(),COOKIE_MONTH);
    }

    @Override
    public GraceResult getAdminList(Integer page, Integer pageSize) {
        if (page==null){
            page = COMMON_START_PAGE;
        }
        if (pageSize==null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PageGridResult result = adminUserService.queryAdminList(page,pageSize);
        return GraceResult.ok(result);
    }

    @Override
    public GraceResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        redis.del(REDIS_ADMIN_TOKEN+":"+adminId);
        deleteCookie(request,response,"aToken");
        deleteCookie(request,response,"aId");
        deleteCookie(request,response,"aName");
        return GraceResult.ok();
    }

    @Override
    public GraceResult updateAdmin(HttpServletRequest request, HttpServletResponse response, NewAdminBO newAdminBO) {
        adminUserService.updateAdmin(newAdminBO.getUsername(),newAdminBO.getFaceId());
        return GraceResult.ok(newAdminBO);
    }

    @Override
    public GraceResult adminFaceLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(adminLoginBO.getUsername())){
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_NULL_ERROR);
        }

        String tempFace64 = adminLoginBO.getImg64();
        if (StringUtils.isBlank(tempFace64)){
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        }

        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        String adminFaceId = admin.getFaceId();
        System.out.println(adminFaceId);
        if (StringUtils.isBlank(adminFaceId)){
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }
        String fileServerUrl = "http://files.sn.com:8004/fs/readFace64?faceId="+adminFaceId;
        ResponseEntity<GraceResult> responseEntity = restTemplate.getForEntity(fileServerUrl,GraceResult.class);
        GraceResult bodyResult = responseEntity.getBody();
        assert bodyResult != null;
        String base64 = (String) bodyResult.getData();
        boolean result = faceVerifyUtil.faceVerify(FaceVerifyType.BASE64.type, tempFace64, base64,60);
        if (!result){
            return GraceResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_LOGIN_ERROR);
        }
        doLoginSettings(admin,request,response);
        return GraceResult.ok();
    }
}
