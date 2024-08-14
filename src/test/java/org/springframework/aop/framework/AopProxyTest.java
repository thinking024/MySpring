package org.springframework.aop.framework;

import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.AdvisedSupport;
import org.springframework.aop.GenericInterceptor;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.common.WorldServiceBeforeAdvice;
import org.springframework.common.WorldServiceInterceptor;
import org.springframework.service.WorldService;
import org.springframework.service.WorldServiceImpl;

import static org.junit.Assert.*;

public class AopProxyTest {
    public AdvisedSupport advisedSupport;

    @Before
    public void setup() {
        WorldService worldService = new WorldServiceImpl();

        // 被代理对象
        TargetSource targetSource = new TargetSource(worldService);

        // 拦截器，这里是环绕通知
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();

        // 切点表达式
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* org.springframework.service.WorldService.explode(..))").getMethodMatcher();

        advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(methodInterceptor);
        advisedSupport.setMethodMatcher(methodMatcher);
    }

    @Test
    public void testJdkDynamicProxy() throws Exception {
        // 通过jdk动态代理生成代理对象
        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testCglibDynamicProxy() {
        // 通过cglib动态代理生成代理对象
        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testBeforeAdvice() throws Exception {
        WorldServiceBeforeAdvice beforeAdvice = new WorldServiceBeforeAdvice();
        GenericInterceptor interceptor = new GenericInterceptor();
        interceptor.setBeforeAdvice(beforeAdvice);

        // 不再使用WorldServiceInterceptor
        advisedSupport.setMethodInterceptor(interceptor);

        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explode();
    }
}