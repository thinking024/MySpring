package org.springframework.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class B {

	private A a;

	public A getA() {
		return a;
	}

	public void setA(A a) {
		this.a = a;
	}
}