package org.springframework.aop;

import org.aopalliance.aop.Advice;

import java.lang.reflect.Method;

public interface AfterAdvice extends Advice {
    void after(Method method, Object[] args, Object target) throws Throwable;
}