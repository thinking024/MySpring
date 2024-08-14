package org.springframework.beans.factory;

/**
 * 带有初始化方法的bean接口
 */
public interface InitializingBean {

	void afterPropertiesSet() throws Exception;
}