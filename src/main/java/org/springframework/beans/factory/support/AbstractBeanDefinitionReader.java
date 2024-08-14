package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * 继承自BeanDefinitionReader接口，提供了BeanDefinitionReader的抽象实现
 * 包含ResourceLoader来获取资源，并将资源中的BeanDefinition注册到BeanDefinitionRegistry中
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private final BeanDefinitionRegistry registry;
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 对location数组中的每个location调用loadBeanDefinitions(String location)方法
     */
    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }
}
