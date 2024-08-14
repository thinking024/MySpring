package org.springframework.common;

import org.springframework.bean.Car;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 自定义BeanPostProcessor，在实例化后，执行初始化方法前后，对bean进行处理
 */
public class CustomerBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
		System.out.println("CustomerBeanPostProcessor#postProcessBeforeInitialization, beanName: " + beanName);
		//换兰博基尼
		if ("car".equals(beanName)) {
			((Car) bean).setBrand("lamborghini");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
		System.out.println("CustomerBeanPostProcessor#postProcessAfterInitialization, beanName: " + beanName);
		return bean;
	}
}