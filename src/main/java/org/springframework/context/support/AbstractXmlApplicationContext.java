package org.springframework.context.support;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 在刷新bean factory的基础上，增加了从xml加载definition的功能
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

	/**
	 * 使用XmlBeanDefinitionReader加载definition
	 */
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			beanDefinitionReader.loadBeanDefinitions(configLocations);
		}
	}

	/**
	 * 获取配置文件路径
	 */
	protected abstract String[] getConfigLocations();
}