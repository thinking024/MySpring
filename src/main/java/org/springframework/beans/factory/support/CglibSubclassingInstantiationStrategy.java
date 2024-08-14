package org.springframework.beans.factory.support;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Cglib动态代理子类化实例化策略
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition) {
        Enhancer enhancer = new Enhancer();

        // 根据bean definition设置父类
        enhancer.setSuperclass(beanDefinition.getBeanClass());

        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            // 这里可以实现AOP
            return proxy.invokeSuper(obj, args);
        });

        return enhancer.create();
    }
}
