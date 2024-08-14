package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的bean工厂，继承单例bean注册表，实现可配置的bean工厂接口
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    /**
     * BeanPostProcessor列表
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Map<String, Object> factoryBeanObjectCache = new HashMap<>();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    private ConversionService conversionService;

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * 重写BeanFactory中获取bean的抽象方法
     */
    @Override
    public Object getBean(String name) throws BeansException {
        // 默认获取单例bean
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            // 如果这个bean是Factory Bean，从中创建bean
            return getObjectForBeanInstance(sharedInstance, name);
        }

        // bean不存在，则创建
        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition);


        // 如果这个bean是Factory Bean，从中创建bean
        return getObjectForBeanInstance(bean, name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return ((T) getBean(name));
    }

    /**
     * 创建factory bean的bean实例
     */
    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        Object object = beanInstance;
        if (beanInstance instanceof FactoryBean) {
            FactoryBean factoryBean = (FactoryBean) beanInstance;
            try {
                if (factoryBean.isSingleton()) {
                    // singleton作用域bean，从缓存中获取
                    object = this.factoryBeanObjectCache.get(beanName);
                    if (object == null) {
                        object = factoryBean.getObject();
                        this.factoryBeanObjectCache.put(beanName, object);
                    }
                } else {
                    // prototype作用域bean，新创建bean
                    object = factoryBean.getObject();
                }
            } catch (Exception ex) {
                throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", ex);
            }
        }

        return object;
    }


    /**
     * 根据bean名称+定义，创建对应bean
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    /**
     * 根据bean名称获取对应的定义definition
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 有则覆盖
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
