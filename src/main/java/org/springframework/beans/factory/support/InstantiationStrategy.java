package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * bean实例化策略接口
 */
public interface InstantiationStrategy {

    /**
     * 根据definition实例化bean的抽象方法
     */
    Object instantiate(BeanDefinition beanDefinition);
}
