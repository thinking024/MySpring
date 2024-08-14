package org.springframework.aop;

import java.lang.reflect.Method;

/**
 * aspectJ的切点表达式，匹配到对应的类+方法
 */
public interface MethodMatcher {

	boolean matches(Method method, Class<?> targetClass);
}