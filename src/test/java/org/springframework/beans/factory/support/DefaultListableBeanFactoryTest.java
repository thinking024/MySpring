package org.springframework.beans.factory.support;

import org.junit.Test;
import org.springframework.bean.Car;
import org.springframework.bean.Person;
import org.springframework.bean.PersonWithCar;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.common.CustomBeanFactoryPostProcessor;
import org.springframework.common.CustomerBeanPostProcessor;
import org.springframework.service.HelloService;

import static org.junit.Assert.*;

public class DefaultListableBeanFactoryTest {
    @Test
    public void testRegisterBeanDefinition() {
        // 生成一个bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 生成一个bean定义
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);

        // 注册bean定义
        beanFactory.registerBeanDefinition("testBean", beanDefinition);
        assertEquals(beanDefinition, beanFactory.getBeanDefinition("testBean"));

        // 测试获取bean
        HelloService helloService = (HelloService) beanFactory.getBean("testBean");
        helloService.sayHello();

        // 测试单例bean
        HelloService helloService1 = (HelloService) beanFactory.getBean("testBean");
        assertEquals(helloService, helloService1);
    }

    @Test
    public void testPopulateProperties() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 生成属性值
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "Jack"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));

        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
    }

    @Test
    public void testPopulateBeanWithBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //注册Car实例
        PropertyValues propertyValuesForCar = new PropertyValues();
        propertyValuesForCar.addPropertyValue(new PropertyValue("brand", "BMW"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, propertyValuesForCar);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);

        // 生成属性值
        PropertyValues propertyValuesForPerson = new PropertyValues();
        propertyValuesForPerson.addPropertyValue(new PropertyValue("name", "Jack"));
        propertyValuesForPerson.addPropertyValue(new PropertyValue("age", 18));

        // 依赖Car
        propertyValuesForPerson.addPropertyValue(new PropertyValue("car", new BeanReference("car")));

        BeanDefinition personBeanDefinition = new BeanDefinition(PersonWithCar.class, propertyValuesForPerson);
        beanFactory.registerBeanDefinition("person", personBeanDefinition);

        PersonWithCar person = (PersonWithCar) beanFactory.getBean("person");
        System.out.println(person);
    }

    /**
     * BeanFactoryPostProcessor需要手动调用，传入BeanFactory
     */
    @Test
    public void testBeanFactoryPostProcessor() throws Exception {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions("classpath:beans.xml");

        // 在所有BeanDefinition加载完成后，但在bean实例化之前，修改BeanDefinition的属性值
        CustomBeanFactoryPostProcessor processor = new CustomBeanFactoryPostProcessor();
        processor.postProcessBeanFactory(factory);

        PersonWithCar person = factory.getBean("personWithCar", PersonWithCar.class);
        System.out.println(person);
        // name属性在CustomBeanFactoryPostProcessor中被修改为ivy
    }

    /**
     * BeanPostProcessor在bean实例化前后自动进行处理
     */
    @Test
    public void testBeanPostProcessor() throws Exception {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions("classpath:beans.xml");

        //添加bean实例化后的处理器
        CustomerBeanPostProcessor processor = new CustomerBeanPostProcessor();
        factory.addBeanPostProcessor(processor);

        Car car = factory.getBean("car", Car.class);
        System.out.println(car);
        //brand属性在CustomerBeanPostProcessor中被修改为lamborghini
    }
}