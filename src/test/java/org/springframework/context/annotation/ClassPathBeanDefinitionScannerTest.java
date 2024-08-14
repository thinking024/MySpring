package org.springframework.context.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ClassPathBeanDefinitionScannerTest {
    @Test
    public void testScanPackage() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:component.xml");
        Car car = applicationContext.getBean("car", Car.class);
        Assert.assertNotNull(car);
        System.out.println(car); // brand为空，因为没有使用@Value注解
    }

}