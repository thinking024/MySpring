package org.springframework.beans.factory.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.bean.A;
import org.springframework.bean.B;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DefaultSingletonBeanRegistryTest {
    @Test
    public void testCircularReference() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:circular-ref.xml");
        A a = applicationContext.getBean("a", A.class);
        B b = applicationContext.getBean("b", B.class);
        Assert.assertSame(a.getB(), b);
    }
}