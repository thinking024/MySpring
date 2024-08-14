package org.springframework.context.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.bean.CarWithConvert;

import java.time.LocalDate;


public class ConversionServiceFactoryBeanTest {
    @Test
    public void testConversionService() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:convert.xml");

        CarWithConvert car = applicationContext.getBean("car", CarWithConvert.class);
        Assert.assertEquals(car.getPrice(), 1000000);
        Assert.assertEquals(car.getProduceDate(), LocalDate.of(2021, 1, 1));
    }

}