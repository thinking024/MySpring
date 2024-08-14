package org.springframework.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class A {

	private B b;

	public B getB() {
		return b;
	}

	public void setB(B b) {
		this.b = b;
	}
}