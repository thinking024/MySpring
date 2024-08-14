package org.springframework.beans.factory;

import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class PropertyPlaceholderConfigurerTest {

    @Test
    public void testResolvePlaceholder() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:property.xml");
        Car car = context.getBean("car", Car.class);
        assert("lamborghini".equals(car.getBrand()));
    }

}