package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;

/**
 * 特殊的bean后置处理器，提供了在bean实例化之前执行的方法
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * 新的实例化方式，代替原本的实例化
	 * @return 修改后的bean实例，若为null，则继续执行bean原本的实例化
	 */
	Object postProcessBeforeInstance(Class<?> beanClass, String beanName) throws BeansException;

	/**
	 * bean实例化后执行
	 * @return 返回false，不执行后续设置属性的逻辑；返回true则继续放行
	 */
	boolean postProcessAfterInstance(Object bean, String beanName) throws BeansException;

	/**
	 * bean实例化之后，设置属性之前执行
	 * @return 修改后的属性值
	 */
	PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName)
			throws BeansException;

	/**
	 * 获取早期bean引用
	 * @return 普通bean，或者是代理bean
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}
}