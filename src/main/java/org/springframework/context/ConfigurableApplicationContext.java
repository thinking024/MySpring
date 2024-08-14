package org.springframework.context;

import org.springframework.beans.BeansException;

/**
 * 配置上下文
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

	/**
	 * 刷新容器
	 */
	void refresh() throws BeansException;

	void close();

	/**
	 * 向虚拟机中注册一个钩子方法，在虚拟机关闭之前执行关闭容器等操作
	 */
	void registerShutdownHook();
}