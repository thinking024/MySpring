package org.springframework.context.event;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class ApplicationContextEventTest {
    @Test
    public void testEventListener() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event.xml");

        // 自定义事件，需要手动发布
        applicationContext.publishEvent(new CustomEvent(applicationContext));

        applicationContext.registerShutdownHook();
    }
}