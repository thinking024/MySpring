package org.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 代理相关的元数据
 */
public class AdvisedSupport {

	/**
	 * 是否使用CGLIB代理
	 */
	private boolean proxyTargetClass;

	/**
	 * 被代理的目标对象
	 */
	private TargetSource targetSource;

	/**
	 * aop alliance的方法拦截器，可以在方法执行前后做一些事情
	 */
	private MethodInterceptor methodInterceptor;

	/**
	 * 方法匹配器
	 */
	private MethodMatcher methodMatcher;

	public TargetSource getTargetSource() {
		return targetSource;
	}

	public void setTargetSource(TargetSource targetSource) {
		this.targetSource = targetSource;
	}

	public MethodInterceptor getMethodInterceptor() {
		return methodInterceptor;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

	public void setMethodMatcher(MethodMatcher methodMatcher) {
		this.methodMatcher = methodMatcher;
	}

	public boolean isProxyTargetClass() {
		return proxyTargetClass;
	}

	public void setProxyTargetClass(boolean proxyTargetClass) {
		this.proxyTargetClass = proxyTargetClass;
	}

}