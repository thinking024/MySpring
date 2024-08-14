package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.ResourceLoader;

/**
 * 面向应用场合的IOC容器，将之前每次创建BeanFactory和加载BeanDefinition的过程封装到一个类中
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {

}