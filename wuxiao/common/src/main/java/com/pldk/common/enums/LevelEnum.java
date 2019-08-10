package com.pldk.common.enums;

import lombok.Getter;

/**
 * 
 * 类描述：用户级别
 * @author wcw
 * @addtime 2019年7月30日
 */
@Getter
public enum LevelEnum {
	FIRST_LEVEL(1, "省级"),
    SECOND_LEVEL(2, "市级/产业"),
    THREE_LEVEL(3, "区县级");
	
	private int code;

    private String message;

    LevelEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
