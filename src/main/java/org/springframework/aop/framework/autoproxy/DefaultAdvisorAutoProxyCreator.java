package org.springframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.*;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 创建自动代理对象的BeanPostProcessor实现，
 * 在初始化后替换原始bean对象为代理对象
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    /**
     * 存放已经代理后的bean name
     */
    private Set<String> earlyProxyReferences = new HashSet<>();

    @Override
    public Object postProcessBeforeInstance(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstance(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }


    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 在bean初始化后，替换原始bean对象为代理对象，无循环依赖时会执行此方法
     * 和下文中的getEarlyBeanReference只会有一个被执行代理逻辑
     */
    @Override
    public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
        // 如果bean已经被代理过了，直接返回，避免重复代理
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    /**
     * 先将bean放入三级缓存，再执行代理逻辑，有循环依赖时才会触发此方法中的代理
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        // 该bean已经代理过了，直接返回
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        // 获取所有注册的Advisor
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType((AspectJExpressionPointcutAdvisor.class)).values();

        try {
            // 遍历Advisor，找到能够匹配当前bean的Advisor，执行代理逻辑
            // 类似于AspectJExpressionPointcutAdvisorTest中的testPointCutAdvisor方法
            for (AspectJExpressionPointcutAdvisor advisor : advisors) {
                ClassFilter classFilter = advisor.getPointcut().getClassFilter();
                if (classFilter.matches(bean.getClass())) {
                    AdvisedSupport support = new AdvisedSupport();
                    support.setTargetSource(new TargetSource(bean));
                    support.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                    support.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                    return new ProxyFactory(support).getProxy();
                }
            }
        } catch (Exception ex) {
            throw new BeansException("Error create proxy bean for: " + beanName, ex);
        }
        return bean;
    }
}
