package org.springframework.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

/**
 * 对象适配器，将没有实现DisposableBean接口的bean对象适配成DisposableBean接口
 */
public class DisposableBeanAdapter implements DisposableBean {

	/**
	 * 真正的bean对象，但是没有实现DisposableBean接口，被适配者
	 */
	private final Object bean;

	private final String beanName;

	private final String destroyMethodName;

	public DisposableBeanAdapter(Object bean, String beanName, String destroyMethodName) {
		this.bean = bean;
		this.beanName = beanName;
		this.destroyMethodName = destroyMethodName;
	}

	@Override
	public void destroy() throws Exception {
		if (bean instanceof DisposableBean) {
			((DisposableBean) bean).destroy();
		}

		// 避免同时继承自DisposableBean，且自定义方法与DisposableBean方法同名，销毁方法执行两次的情况
		if (StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destroy".equals(this.destroyMethodName))) {
			// 执行自定义方法
			Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
			if (destroyMethod == null) {
				throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
			}
			destroyMethod.invoke(bean);
		}
	}
}