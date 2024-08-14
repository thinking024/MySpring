package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition注册表接口
 */
public interface BeanDefinitionRegistry {

    /**
     * 向注册表中注册Bean Definition的抽象方法
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 根据bean名称获取对应的定义definition
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    boolean containsBeanDefinition(String beanName);

    /**
     * 返回注册表中所有bean的名称
     */
    String[] getBeanDefinitionNames();
}
