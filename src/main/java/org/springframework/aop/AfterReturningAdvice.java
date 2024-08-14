package org.springframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

public interface AfterReturningAdvice extends Advice {

    /**
     * 多了一个returnValue参数，表示目标方法的返回值
     */
    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}