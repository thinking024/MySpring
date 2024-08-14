package org.springframework.beans.factory.config;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

/**
 * 可配置的bean工厂接口，能够添加BeanPostProcessor，销毁单例bean
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析嵌入的值
     */
    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();
}