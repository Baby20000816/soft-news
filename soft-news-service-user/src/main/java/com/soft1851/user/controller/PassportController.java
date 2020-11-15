package com.soft1851.user.controller;

import com.soft1851.api.BaseController;
import com.soft1851.api.controller.user.PassportControllerApi;
import com.soft1851.result.GraceResult;
import com.soft1851.utils.IpUtil;
import com.soft1851.utils.SmsUtil;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {
    @Resource
    private SmsUtil smsUtil;

    @Override
    public GraceResult getCode(String mobile, HttpServletRequest request) {
        String userIp = IpUtil.getRequestIp(request);
        redis.setnx60s(MOBILE_SMSCODE+":"+userIp, userIp);
        String random = (int)((Math.random()*9+1)*10000)+"";
        smsUtil.sendSms(mobile,random);
        redis.set(MOBILE_SMSCODE+":"+mobile,random,30*60);
        return GraceResult.ok();
    }
}
