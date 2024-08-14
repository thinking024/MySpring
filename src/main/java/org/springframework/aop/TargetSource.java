package org.springframework.aop;

/**
 * 被代理的目标对象，封装了目标对象的信息
 */
public class TargetSource {

	private final Object target;

	public TargetSource(Object target) {
		this.target = target;
	}

	/**
	 * 获取目标对象继承的所有接口
	 */
	public Class<?>[] getTargetClass() {
		return this.target.getClass().getInterfaces();
	}

	public Object getTarget() {
		return this.target;
	}

}