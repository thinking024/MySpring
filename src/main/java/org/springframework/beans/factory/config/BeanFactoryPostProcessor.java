package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * 在bean实例化前修改bean definition
 */
public interface BeanFactoryPostProcessor {

        /**
        * 传入beanFactory，修改其中的bean definition
        */
        void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
