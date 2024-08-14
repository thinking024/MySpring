package org.springframework.context.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 注册监听器+发布事件的接口
 */
public interface ApplicationEventMulticaster {

	void addApplicationListener(ApplicationListener<?> listener);

	void removeApplicationListener(ApplicationListener<?> listener);

	void multicastEvent(ApplicationEvent event);

}