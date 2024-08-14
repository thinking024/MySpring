package org.springframework.aop.framework.autoproxy;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.service.WorldService;


public class DefaultAdvisorAutoProxyCreatorTest {
    @Test
    public void testAutoProxy() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:auto-proxy.xml");

        //获取代理对象
        WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
        worldService.explode();
    }
}