package org.springframework.beans.factory.config;

/**
 * 单例bean注册表的抽象接口
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例bean
     */
    Object getSingleton(String beanName);

    void addSingleton(String beanName, Object singletonObject);
}
