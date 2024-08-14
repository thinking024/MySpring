package org.springframework.context;

import java.util.EventObject;

/**
 * 事件抽象类
 */
public abstract class ApplicationEvent extends EventObject {

	/**
	 * 将事件源作为构造参数传入
	 * @param source 触发事件的对象
	 */
	public ApplicationEvent(Object source) {
		super(source);
	}
}