package org.springframework.beans.factory;

/**
 * 特殊的bean，它本身是一个工厂，通过getObject方法返回工厂创建的bean实例
 * @param <T>
 */
public interface FactoryBean<T> {

	/**
	 * 获取工厂创建的bean实例
	 */
	T getObject() throws Exception;

	boolean isSingleton();
}