package com.pldk.common.constant;

/**
 * 数据状态常量
 * @author Pldk
 * @date 2019/2/22
 */
public class StatusConst {

    // 正常状态码
    public static final byte OK = 1;
    // 冻结状态码
    public static final byte FREEZED = 2;
    // 删除状态码
    public static final byte DELETE = 3;

    
    // 区县待上报
    public static final String AUDIT_REPORT = "0";
    // 市级待审核状态码
    public static final String FIRST_AUDIT_WAIT = "1";
    // 市级已通过状态码
    public static final String FIRST_AUDIT_PASS = "2";
    // 市级已驳回状态码
    public static final String FIRST_AUDIT_FAIL = "3";
    // 市级已退回--不能再次上报状态码
    public static final String FIRST_AUDIT_DEAL = "10";
    // 省级待审核状态码
    public static final String AUDIT_WAIT = "4";
    // 省级已通过状态码
    public static final String AUDIT_PASS = "5";
    // 省级已驳回状态码
    public static final String AUDIT_FAIL = "6";
    // 省级已退回--不能再次上报状态码
    public static final String AUDIT_DEAL = "11";
    // 待评分状态码(一级上报)
    public static final String SCORE_WAIT = "7";
    // 已评分状态码(专家评分)
    public static final String SCORE_OK = "8";
    // 未评分状态码(专家评分)
    public static final String SCORE_NO = "9";
}
