package com.pldk.component.actionLog.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pldk.common.exception.advice.ResultExceptionAdvice;

/**
 * 将异常切入程序添加到异常通知器中
 * @author Pldk
 * @date 2019/4/6
 */
@Configuration
public class ActionLogProceedAdviceConfig {

    @Bean
    public ActionLogProceedAdvice actionLogProceedAdvice(ResultExceptionAdvice advice) {
        ActionLogProceedAdvice authorization = new ActionLogProceedAdvice();
        advice.putProceed(authorization);
        return authorization;
    }
}
