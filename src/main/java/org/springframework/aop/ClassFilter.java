package org.springframework.aop;

/**
 * aspectJ的切点表达式，匹配到对应的类
 */
public interface ClassFilter {

	boolean matches(Class<?> clazz);
}