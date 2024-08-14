package org.springframework.common;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class WorldServiceInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("Before invoke explode");
        Object result = methodInvocation.proceed();
        System.out.println("After invoke explode");
        return result;
    }
}
