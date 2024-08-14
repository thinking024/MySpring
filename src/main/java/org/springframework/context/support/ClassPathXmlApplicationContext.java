package org.springframework.context.support;

import org.springframework.beans.BeansException;

/**
 * 在xml文件加载的基础上，定义了文件的location
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

	private String[] configLocations;

	/**
	 * 接收单独的location
	 */
	public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[]{configLocation});
	}

	/**
	 * 接收location数组
	 */
	public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
		this.configLocations = configLocations;
		refresh();
	}

	protected String[] getConfigLocations() {
		return this.configLocations;
	}
}