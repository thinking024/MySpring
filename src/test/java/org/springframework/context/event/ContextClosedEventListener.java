package org.springframework.context.event;

import org.springframework.context.ApplicationListener;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println(this.getClass().getName());
	}
}