package org.springframework.context;

/**
 * 发布事件的接口
 */
public interface ApplicationEventPublisher {

	/**
	 * 发布事件
	 */
	void publishEvent(ApplicationEvent event);
}