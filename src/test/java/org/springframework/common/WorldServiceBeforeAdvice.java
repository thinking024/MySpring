package org.springframework.common;

import org.springframework.aop.BeforeAdvice;

import java.lang.reflect.Method;

public class WorldServiceBeforeAdvice implements BeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("BeforeAdvice: do something before the earth explodes");
	}
}