package org.springframework.aop.aspectj;

import org.junit.Test;
import org.springframework.service.HelloService;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class AspectJExpressionPointcutTest {
    @Test
    public void testPointcutExpression() throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.springframework.service.HelloService.*(..))");

        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");

        // 切点表达式能正确匹配到类和方法
        assert(pointcut.matches(clazz));
        assert(pointcut.matches(method, clazz));
    }
}