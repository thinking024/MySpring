package org.springframework.context.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 在AbstractApplicationEventMulticaster的基础上，实现了广播事件的功能
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

	public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
		setBeanFactory(beanFactory);
	}

	/**
	 * 广播事件，依次遍历监听器，调用监听器的onApplicationEvent方法
	 */
	@Override
	public void multicastEvent(ApplicationEvent event) {
		for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
			if (supportsEvent(applicationListener, event)) {
				applicationListener.onApplicationEvent(event);
			}
		}
	}

	/**
	 * 监听器是否对该事件感兴趣
	 */
	protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
		Type type = applicationListener.getClass().getGenericInterfaces()[0];
		Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
		String className = actualTypeArgument.getTypeName();
		Class<?> eventClassName;
		try {
			eventClassName = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BeansException("wrong event class name: " + className);
		}
		return eventClassName.isAssignableFrom(event.getClass());
	}
}