package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ApplicationContextAwareProcessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象应用上下文，能够加载xml等资源
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        refreshBeanFactory(); // 实现见子类AbstractRefreshableApplicationContext
        ConfigurableListableBeanFactory beanFactory = getBeanFactory(); // 实现见子类AbstractRefreshableApplicationContext

        // 让继承自ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 在bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 其他的BeanPostProcessor需要在其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 初始化事件广播器
        initApplicationEventMulticaster();

        // 注册监听器
        registerListeners();

        // 执行初始化
        finishBeanFactoryInitialization(beanFactory);

        // 发布容器刷新事件
        finishRefresh();
    }

    /**
     * 创建BeanFactory，并加载BeanDefinition
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    /**
     * 注册BeanPostProcessor
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取definition中定义的BeanPostProcessor这个类型的bean
        Collection<BeanPostProcessor> values = beanFactory.getBeansOfType(BeanPostProcessor.class).values();

        // 添加到BeanFactory中，在bean初始化的时候会自动执行
        for (BeanPostProcessor beanPostProcessor : values) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 在bean实例化之前，执行BeanFactoryPostProcessor
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取definition中定义的BeanFactoryPostProcessor这个类型的bean
        Collection<BeanFactoryPostProcessor> values = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values();

        // BeanFactoryPostProcessor需要手动调用执行
        for (BeanFactoryPostProcessor postProcessor : values) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 初始化广播器，并注册到bean factory中
     */
    protected void initApplicationEventMulticaster() {
        // 获取bean factory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 创建事件广播器
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);

        // 注册为单例bean
        beanFactory.addSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册监听器到广播器中
     */
    protected void registerListeners() {
        // 获取所有的ApplicationListener类型的bean
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();

        // 注册到事件广播器中
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    /**
     * 发布容器刷新事件ContextRefreshedEvent
     */
    protected void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    public void close() {
        doClose();
    }

    protected void doClose() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        destroyBeans();
    }

    /**
     * 委托给BeanFactory，执行它的destroySingletons方法
     */
    protected void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::doClose);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * 完成bean factory的初始化，并提前实例化单例bean
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        // 设置类型转换器
        if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME)) {
            Object conversionService = beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME);
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }

        // 提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }
    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

}
