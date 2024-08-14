package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例bean注册表的默认实现
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 存储单例bean的缓存
     */
    private Map<String, Object> singletonObjects = new HashMap<>();

    /**
     * 二级缓存，能让已实例化正在设置属性的bean提前暴露给其他bean使用，
     * 可解决无代理的循环依赖
     */
    private Map<String, Object> earlySingletonObjects = new HashMap<>();

    /**
     * 三级缓存，存储创建早期原始对象bean的工厂，
     * 可解决有代理的循环依赖
     */
    private Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    /**
     * 带destroy方法的bean的缓存
     */
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    /**
     * 依次检查缓存，获取单例bean
     */
    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    // 从三级缓存放进二级缓存，代表这个bean已经实例化完成，但是属性还未填充，且已经暴露给其他bean使用
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    /**
     * 注册单例bean，即将beanName和singletonObject放入一级缓存，
     * 同时清除二级缓存和三级缓存
     */
    public void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        singletonFactories.put(beanName, singletonFactory);
    }


    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
        for (String beanName : beanNames) {
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
