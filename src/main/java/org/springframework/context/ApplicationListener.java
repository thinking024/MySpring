package org.springframework.context;

import java.util.EventListener;

/**
 * 监听事件的接口
 * @param <E> 要监听的事件类型
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	void onApplicationEvent(E event);
}