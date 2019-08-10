package com.pldk.common.enums;

import lombok.Getter;

/**
 * 单位类型枚举
 * @author Pldk
 * @date 2018/8/14
 */
@Getter
public enum UnitTypeEnum {

	LABORUN_UNIT_TYPE(1, "工会"),
    GROUP_UNIT_TYPE(2, "团省委"),
    SCIENCE_UNIT_TYPE(3, "省科协");
	
	private int code;

    private String message;

    UnitTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
