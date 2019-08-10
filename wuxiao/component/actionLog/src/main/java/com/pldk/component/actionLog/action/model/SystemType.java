package com.pldk.component.actionLog.action.model;

import com.pldk.modules.system.enums.ActionLogEnum;

import lombok.Getter;

/**
 * @author Pldk
 * @date 2018/10/15
 */
@Getter
public class SystemType extends BusinessType{
    // 日志类型
    protected Byte type = ActionLogEnum.SYSTEM.getCode();

    public SystemType(String message) {
        super(message);
    }

    public SystemType(String name, String message) {
        super(name, message);
    }
}
