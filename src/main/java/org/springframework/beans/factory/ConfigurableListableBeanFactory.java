package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * 根据名称查找BeanDefinition
	 */
	BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	/**
	 * 预实例化所有单例
	 */
	void preInstantiateSingletons() throws BeansException;

	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}