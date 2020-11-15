package com.soft1851.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.soft1851.utils.extend.AliyunResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SmsUtil {
    @Resource
    public AliyunResource aliyunResource;
    public void sendSms(String mobile,String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",aliyunResource.getAccessKeyId(),aliyunResource.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId","cn-hangzhou");
        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName","颜氏企业");
        request.putQueryParameter("TemplateCode","SMS_205393095");
        request.putQueryParameter("TemplateParam","{\"code\":\""+code+"\"}");

        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        }catch (ClientException e){
            System.err.println(e.getMessage());
        }
    }
}
