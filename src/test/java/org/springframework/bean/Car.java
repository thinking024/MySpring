package org.springframework.bean;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 直接继承自InitializingBean和DisposableBean，实现了初始化和销毁方法
 */
@Component
public class Car implements InitializingBean, DisposableBean {

	@Value("${brand}")
	private String brand;

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "Car{" +
				"brand='" + brand + '\'' +
				'}';
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("Car.destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Car.afterPropertiesSet");
	}
}
