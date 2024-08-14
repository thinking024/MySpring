package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface BeanFactory {

    /**
     * 根据bean名称获取bean的抽象方法
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据bean名称和类型获取bean的抽象方法
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据类型获取bean的抽象方法
     * @throws BeansException 如果bean不存在或者存在多个同类型的bean
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    boolean containsBean(String name);
}
