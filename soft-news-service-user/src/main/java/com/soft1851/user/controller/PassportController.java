package com.soft1851.user.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.PassportControllerApi;
import com.soft1851.enums.UserStatus;
import com.soft1851.pojo.AppUser;
import com.soft1851.pojo.bo.RegistLoginBO;
import com.soft1851.result.GraceResult;
import com.soft1851.result.ResponseStatusEnum;
import com.soft1851.user.service.UserService;
import com.soft1851.utils.IpUtil;
import com.soft1851.utils.JsonUtil;
import com.soft1851.utils.SmsUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.n3r.idworker.Sid;
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassportController extends BaseController implements PassportControllerApi {
    @Resource
    private final SmsUtil smsUtil;
    private final UserService userService;

    @Override
    public GraceResult getCode(String mobile, HttpServletRequest request) {
        String userIp = IpUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE+":"+userIp, userIp);
        String random = (int)((Math.random()*9+1)*10000)+"";
        smsUtil.sendSms(mobile,random);
        redis.set(MOBILE_SMSCODE+":"+mobile,random,30*60);
        return GraceResult.ok();
    }

    @Override
    public GraceResult doSign(@Valid RegistLoginBO registLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()){
            Map<String,String> map = getErrors(result);
            return GraceResult.errorMap(map);
        }
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        String redisSmsCode = redis.get(MOBILE_SMSCODE+":"+mobile);
        if (StringUtils.isBlank(redisSmsCode)||!redisSmsCode.equalsIgnoreCase(smsCode)){
            return GraceResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        AppUser user = userService.queryMobileIsExist(mobile);
        if (user!=null&& user.getActiveStatus().equals(UserStatus.FROZEN.type)){
            return GraceResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }else if (user == null){
            user = userService.createUser(mobile);
        }
        int userActiveStatus = user.getActiveStatus();
        if (userActiveStatus!= UserStatus.FROZEN.type){
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN +":"+user.getId(),uToken);
            redis.set(REDIS_USER_INFO+":"+user.getId(), JsonUtil.objectToJson(user));
            setCookie(request,response,"utoken",uToken,COOKIE_MONTH);
            setCookie(request,response,"uid",user.getId(),COOKIE_MONTH);
        }
        redis.del(MOBILE_SMSCODE +":"+mobile);
        return GraceResult.ok(userActiveStatus);
    }

    @Override
    public GraceResult logout(HttpServletRequest request, HttpServletResponse response, String userId) {
        redis.del(REDIS_USER_TOKEN+":"+userId);
        setCookie(request,response,"utoken","",COOKIE_DELETE);
        setCookie(request,response,"uid","",COOKIE_DELETE);
        return GraceResult.ok();
    }
}
