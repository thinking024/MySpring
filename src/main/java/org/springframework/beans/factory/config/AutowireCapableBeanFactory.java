package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

/**
 * 自动配置Bean工厂接口，定义了对Bean实例进行自动装配的方法，包括应用BeanPostProcessor的回调方法
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 为给定的现有Bean实例应用BeanPostProcessor的PostProcessorsBeforeInitialization回调
     */
    Object applyBeanPostProcessorsBeforeInit(Object existingBean, String beanName) throws BeansException;

    /**
	 * 为给定的现有Bean实例应用BeanPostProcessor的PostProcessorsAfterInitialization回调
	 */
    Object applyBeanPostProcessorsAfterInit(Object existingBean, String beanName) throws BeansException;

}