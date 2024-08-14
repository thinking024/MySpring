package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.AbstractApplicationContext;

/**
 * 抽象可刷新的应用上下文，内部持有bean factory属性，实现了刷新/创建bean factory的方法
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    /**
     * 内部持有的BeanFactory属性
     */
    private DefaultListableBeanFactory beanFactory;

    /**
     * 重写父类AbstractApplicationContext方法，
     * 创建BeanFactory，并加载BeanDefinition，
     * 然后赋值给内部持有的beanFactory属性
     */
    @Override
    protected void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 重写父类AbstractApplicationContext方法，
     * 返回内部持有的BeanFactory属性
     * @return
     */
    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 加载BeanDefinition，具体实现交给子类
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;

}
