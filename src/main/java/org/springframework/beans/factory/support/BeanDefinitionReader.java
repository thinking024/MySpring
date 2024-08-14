package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 读取BeanDefinition的接口
 */
public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();
    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(String location) throws BeansException;
    void loadBeanDefinitions(String[] locations) throws BeansException;
    void loadBeanDefinitions(Resource resource) throws BeansException;
}
