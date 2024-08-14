package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * 用于执行目标对象的代理前的原方法，
 * 包含被调用的方法、target对象、参数等信息
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

	protected final Object target;

	protected final Method method;

	protected final Object[] arguments;

	/**
	 * 构造方法
	 * @param target 被代理的目标对象
	 * @param method 被代理的模板对象的方法
	 * @param arguments 方法参数
	 */
	public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
		this.target = target;
		this.method = method;
		this.arguments = arguments;
	}

	/**
	 * 按aop alliance的规范，通过反射执行代理前的原方法
	 */
	@Override
	public Object proceed() throws Throwable {
		return method.invoke(target, arguments);
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public Object getThis() {
		return target;
	}

	@Override
	public AccessibleObject getStaticPart() {
		return method;
	}
}