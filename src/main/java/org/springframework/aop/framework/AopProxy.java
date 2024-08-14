package org.springframework.aop.framework;

/**
 * 获取代理对象的抽象接口
 */
public interface AopProxy {

	/**
	 * 获取代理对象
	 */
	Object getProxy();
}