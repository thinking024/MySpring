package org.springframework.context.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * 应用上下文事件抽象类
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

	/**
	 * 将应用上下文作为事件源
	 */
	public ApplicationContextEvent(ApplicationContext source) {
		super(source);
	}

	/**
	 * 获取应用上下文容器对象，即触发事件的事件源
	 */
	public final ApplicationContext getApplicationContext() {
		return (ApplicationContext) getSource();
	}
}