package com.soft1851.enums;

import org.omg.PortableInterceptor.INACTIVE;

/**
 * @Desc: 用户状态 枚举
 */
public enum UserStatus {
//    用户状态:
//    0: 未激活,
//    1: 已激活，基本信息是否完善，真实姓名，邮箱地址，性别，生日，地址等。
//        如果没有完善，则用户不能发表评论，不能点赞，不能关注，不能操作任何入库的功能
//      2: 已冻结

    INACTIVE(0, "未激活"),
    Active(1, "已激活"),
    FROZEN(2, "已冻结");

    public final Integer type;
    public  final String value;

    UserStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == INACTIVE.type || tempStatus == Active.type || tempStatus == FROZEN.type) {
                return true;
            }
        }
        return false;
    }
}
