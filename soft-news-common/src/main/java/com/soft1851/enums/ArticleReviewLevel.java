package com.soft1851.enums;


public enum ArticleReviewLevel {
//    public static final Object PASS = ;
    BLOCK("block", "自动审核不通过"),
    PASS("pass", "自动审核通过"),
    REVIEW("review", "建议人工复审");

    public final String type;
    public  final String value;

    ArticleReviewLevel(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
