package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 可列出的BeanFactory的默认实现，兼具注册bean definition和自动装配bean的功能
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry{
    /**
     * 存储bean定义的注册表
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 重写AbstractBeanFactory中获取bean定义的方法，从beanDefinitionMap中获取
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named " + beanName + " is defined");
        }
        return beanDefinition;
    }

    /**
     * 重写BeanDefinitionRegistry中判断是否包含bean定义的方法，即判断注册表beanDefinitionMap中是否包含该beanName
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * 重写BeanDefinitionRegistry中注册Bean Definition的方法，即将其放入beanDefinitionMap缓存
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }


    /**
     * 重写ListableBeanFactory中获取指定类型的所有实例的方法，遍历beanDefinitionMap，找到类型符合的bean，然后调用getBean方法获取bean实例
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    /**
     * 重写ListableBeanFactory中获取所有bean定义名称的方法，即返回注册表beanDefinitionMap中所有的key
     */
    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNames = beanDefinitionMap.keySet();
        return beanNames.toArray(new String[0]);
    }

    /**
     * 预实例化所有非lazy-init的单例
     */
    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.isSingleton() && !beanDefinition.isLazyInit()) {
                getBean(beanName);
            }
        });
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        String beaName = null;
        int count = 0;
        // 遍历definition，找到Class符合的bean，然后调用getBean方法获取bean实例
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                count++;
                beaName = entry.getKey();
            }
        }
        if (count == 1) {
            return getBean(beaName, requiredType);
        }
        throw new BeansException(requiredType + "expected single bean but found " + count + ": " + beanDefinitionMap.keySet());
    }
}
