package org.springframework.bean;

/**
 * 普通的bean，自定义的初始化和销毁方法需要在xml中注册
 */
public class Person {

	private String name;

	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}

	public void customInitMethod() {
		System.out.println("Person.customInitMethod");
	}

	public void customDestroyMethod() {
		System.out.println("Person.customDestroyMethod");
	}
}
