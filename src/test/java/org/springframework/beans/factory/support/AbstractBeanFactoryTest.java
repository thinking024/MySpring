package org.springframework.beans.factory.support;

import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AbstractBeanFactoryTest {
    @Test
    public void testFactoryBean() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:beans.xml");
        Car car = applicationContext.getBean("carFactory", Car.class);
        System.out.println(car);
    }
}