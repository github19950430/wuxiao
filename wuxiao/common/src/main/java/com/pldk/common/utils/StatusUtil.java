package com.pldk.common.utils;

import com.pldk.common.constant.StatusConst;
import com.pldk.common.enums.ResultEnum;
import com.pldk.common.enums.StatusEnum;
import com.pldk.common.exception.ResultException;

/**
 * 数据状态工具
 * @author Pldk
 * @date 2019/2/19
 */
public class StatusUtil {

    // 逻辑删除语句
    public static final String sliceDelete = " set status=" + StatusConst.DELETE + " WHERE id=?";

    // 不等于逻辑删除条件语句
    public static final String notDelete = "status != " + StatusConst.DELETE;

    /**
     * 获取状态StatusEnum对象
     * @param param 状态字符参数
     */
    public static StatusEnum getStatusEnum(String param){
        try {
            return StatusEnum.valueOf(param.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResultException(ResultEnum.STATUS_ERROR);
        }
    }
}
