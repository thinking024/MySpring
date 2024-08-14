package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 继承自BeanPostProcessor，用于bean感知所属的ApplicationContext
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

	private final ApplicationContext applicationContext;

	public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 在bean实例化后，初始化前执行，为其设置所属的ApplicationContext
	 */
	@Override
	public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(applicationContext);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
		return bean;
	}
}