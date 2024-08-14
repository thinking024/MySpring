package org.springframework.aop;

/**
 * 切点接口，用于匹配类和方法
 */
public interface Pointcut {

	ClassFilter getClassFilter();

	MethodMatcher getMethodMatcher();
}