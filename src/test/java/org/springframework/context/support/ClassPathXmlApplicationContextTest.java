package org.springframework.context.support;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.bean.Person;
import org.springframework.bean.PersonWithCar;
import org.springframework.service.HelloService;


public class ClassPathXmlApplicationContextTest {

    /**
     * 无需手动调用Post Processor，
     * 从xml中加载bean definition时会一并读取到Post Processor，
     * 然后在AbstractApplicationContext中执行
     */
    @Test
    public void testPostProcessor() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
        Car car = (Car) context.getBean("car");
        System.out.println(car);

        PersonWithCar person = (PersonWithCar) context.getBean("personWithCar");
        System.out.println(person);
    }

    @Test
    public void testInitAndDestroy() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
        Car car = (Car) context.getBean("car");
        Person person = context.getBean("person", Person.class);

        context.close();
    }

    @Test
    public void testAware() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:beans.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);

        Assert.assertNotNull(helloService.getApplicationContext());
        Assert.assertNotNull(helloService.getBeanFactory());
    }

    @Test
    public void testScope() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
        Car car1 = context.getBean("carPrototype", Car.class);
        Car car2 = context.getBean("carPrototype", Car.class);
        System.out.println(car1 == car2);
    }
}