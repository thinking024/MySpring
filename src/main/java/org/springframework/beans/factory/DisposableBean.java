package org.springframework.beans.factory;

/**
 * 带有销毁方法的bean接口
 */
public interface DisposableBean {

	void destroy() throws Exception;
}