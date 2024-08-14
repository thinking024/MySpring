package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

import java.util.Map;

/**
 * 在bean factory的基础上，能列出所有bean的名称和类型
 */
public interface ListableBeanFactory extends BeanFactory {

	/**
	 * 返回指定类型的所有实例
	 */
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

	/**
	 * 返回定义的所有bean的名称
	 */
	String[] getBeanDefinitionNames();
}
