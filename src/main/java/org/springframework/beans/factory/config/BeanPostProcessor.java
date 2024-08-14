package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * bean实例化后修改/替换bean的拓展点
 */
public interface BeanPostProcessor {

        /**
        * 在bean的初始化方法调用之前执行，传入bean，直接修改然后返回
        */
        Object postProcessBeforeInit(Object bean, String beanName) throws BeansException;

        /**
        * 在bean的初始化方法调用之后执行，传入bean，直接修改然后返回
        */
        Object postProcessAfterInit(Object bean, String beanName) throws BeansException;
}
