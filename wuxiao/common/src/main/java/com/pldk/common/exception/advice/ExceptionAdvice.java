package com.pldk.common.exception.advice;

/**
 * 异常通知器接口
 * @author Pldk
 * @date 2019/4/5
 */
public interface ExceptionAdvice {
    public void run(RuntimeException e);
}
