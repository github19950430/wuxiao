package com.pldk.common.enums;

import com.pldk.common.constant.StatusConst;

import lombok.Getter;

/**
 * 数据库字段状态枚举
 * @author Pldk
 * @date 2018/8/14
 */
@Getter
public enum StatusEnum {

    OK(StatusConst.OK, "启用"),
    FREEZED(StatusConst.FREEZED, "冻结"),
    DELETE(StatusConst.DELETE, "删除"),
    AUDIT_REPORT(StatusConst.AUDIT_REPORT, "待上报"),
    FIRST_AUDIT_WAIT(StatusConst.FIRST_AUDIT_WAIT, "待初审"),
    FIRST_AUDIT_PASS(StatusConst.FIRST_AUDIT_PASS, "已通过"),
    FIRST_AUDIT_FAIL(StatusConst.FIRST_AUDIT_FAIL, "未通过"),
    FIRST_AUDIT_DEAL(StatusConst.FIRST_AUDIT_DEAL, "未通过,不能再次上报"),
	AUDIT_WAIT(StatusConst.AUDIT_WAIT, "未审核"),
	AUDIT_PASS(StatusConst.AUDIT_PASS, "已审核"),
	AUDIT_FAIL(StatusConst.AUDIT_FAIL, "已拒绝"),
	AUDIT_DEAL(StatusConst.AUDIT_DEAL, "已拒绝,不能再次上报"),
	SCORE_WAIT(StatusConst.SCORE_WAIT, "待评分"),
	SCORE_OK(StatusConst.SCORE_OK, "已评分"),
	SCORE_NO(StatusConst.SCORE_NO, "未评分");
	

    private Byte code;

    private String value;
    
    private String message;

    StatusEnum(Byte code, String message) {
        this.code = code;
        this.message = message;
    }

	StatusEnum(String value, String message) {
		this.value = value;
		this.message = message;
	}
    
}

