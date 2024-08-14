package org.springframework.aop.framework;

import org.junit.Test;
import org.springframework.service.WorldService;


public class ProxyFactoryTest extends AopProxyTest {

    @Test
    public void testJdkDynamicProxy() throws Exception {
        // 使用JDK动态代理
        advisedSupport.setProxyTargetClass(false);
        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explode();
    }

    @Test
    public void testCglibDynamicProxy() {
        // 使用CGLIB动态代理
        advisedSupport.setProxyTargetClass(true);
        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explode();
    }
}