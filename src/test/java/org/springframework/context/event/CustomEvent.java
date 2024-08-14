package org.springframework.context.event;

import org.springframework.context.ApplicationContext;

public class CustomEvent extends ApplicationContextEvent {

	public CustomEvent(ApplicationContext source) {
		super(source);
	}
}