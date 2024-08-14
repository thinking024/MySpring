package org.springframework.beans.factory.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.bean.PersonWithCar;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class AutowiredAnnotationBeanPostProcessorTest {
    @Test
    public void testValueAnnotation() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:component.xml");

        Car car = applicationContext.getBean("car", Car.class);
        assertEquals("lamborghini", car.getBrand());
    }

    @Test
    public void testAutowiredAnnotation() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:component.xml");
        PersonWithCar personWithCar = applicationContext.getBean("personWithCar", PersonWithCar.class);
        assertNotNull(personWithCar.getCar());
        System.out.println(personWithCar);
    }
}