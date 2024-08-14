package org.springframework.beans;

/**
 * bean的属性值
 * 自jdk16开始，record可以代替这个类，只需要声明属性即可，所有getter、equals、hashCode、toString方法都会自动生成
 */
public class PropertyValue {

	private final String name;

	private final Object value;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
}