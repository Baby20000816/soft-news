package com.soft1851.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistLoginBO {
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;
}
