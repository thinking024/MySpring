package org.springframework.beans.factory.xml;

import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.bean.PersonWithCar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class XMLBeanDefinitionReaderTest {
    @Test
    public void test() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        reader.loadBeanDefinitions("classpath:beans.xml");
        Car car = (Car) factory.getBean("car");
        System.out.println(car);

        PersonWithCar person = (PersonWithCar) factory.getBean("personWithCar");
        System.out.println(person);
    }

}