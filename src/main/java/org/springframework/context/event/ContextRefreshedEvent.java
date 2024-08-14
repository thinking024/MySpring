package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

/**
 * 当ApplicationContext初始化或刷新（调用refresh）时发布的事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

	public ContextRefreshedEvent(ApplicationContext source) {
		super(source);
	}
}