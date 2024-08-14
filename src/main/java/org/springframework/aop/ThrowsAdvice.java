package org.springframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

public interface ThrowsAdvice extends Advice {
    /**
     * 多了一个Throwable参数，用于接收目标方法抛出的异常
     */
    void throwsHandle(Throwable throwable, Method method, Object[] args, Object target);
}