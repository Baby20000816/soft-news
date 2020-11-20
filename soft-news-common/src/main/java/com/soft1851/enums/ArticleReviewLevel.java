package com.soft1851.enums;


public enum ArticleReviewLevel {
//    public static final Object PASS = ;
    INACTIVE("0", "未激活"),
    PASS("1", "已激活"),
    FROZEN("2", "已冻结");

    public final String type;
    public  final String value;

    ArticleReviewLevel(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public static boolean isUserStatusValid(String tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == INACTIVE.type || tempStatus == PASS.type || tempStatus == FROZEN.type) {
                return true;
            }
        }
        return false;
    }
}
