package com.pldk.common.enums;

import com.pldk.common.constant.AuditConst;

import lombok.Getter;

/**
 * 数据库字段状态枚举
 * @author Pldk
 * @date 2018/8/14
 */
@Getter
public enum AuditEnum {
	
	RESULT(AuditConst.RESULT, "成果");

    private Byte code;

    private int value;
    
    private String message;

    AuditEnum(Byte code, String message) {
        this.code = code;
        this.message = message;
    }

    AuditEnum(int value, String message) {
		this.value = value;
		this.message = message;
	}
}
