package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.*;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Method;

/**
 * 可自动装配的抽象BeanFactory=AbstractBeanFactory + AutowireCapableBeanFactory
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /**
     * 实例化策略，默认使用简单的无参构造函数实例化
     */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * 重写AbstractBeanFactory中创建bean的抽象方法
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        // 如果bean需要代理，则直接短路，返回代理对象
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition);
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstance(beanDefinition.getBeanClass(), beanName);
        // 不代理
        if (bean == null) {
            return null;
        }
        // 已经实例化代理后的bean了，直接执行 bean post processor的后置处理
        bean = applyBeanPostProcessorsAfterInit(bean, beanName);
        return bean;
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;
        try {
            bean = createBeanInstance(beanDefinition); // 使用不同的策略决定实例化方式

            // 单例bean的提前暴露，将能生成代理bean的工厂提前放入三级缓存
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, new ObjectFactory<Object>() {
                    @Override
                    public Object getObject() throws BeansException {
                        return getEarlyBeanReference(beanName, beanDefinition, finalBean);
                    }
                });
            }

            // 下面对bean的操作会同步影响到缓存中bean，因为是引用传递

            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstance(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }

            // 修改bean对象的属性值，包括对Value、Autowired注解的处理
            applyBeanPostprocessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

            applyPropertyValues(beanName, beanDefinition, bean); // 为bean对象填充属性

            // 执行bean对象的初始化方法（与实例化区分）
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed: " + beanName, e);
        }

        // 注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
            // todo 此处导致循环依赖问题
            // 因为逻辑上是先设置bean的属性值，再将其放入一级缓存

            // 如果该对象被代理
            // 则在之前的DefaultAdvisorAutoProxyCreator后置处理中不会再次代理，因为earlyProxyReferences中执行了代理操作
            // 此处获取到的exposedObject就是代理对象，否则就是原始对象
            exposedObject = getSingleton(beanName);
            addSingleton(beanName, exposedObject);
        }
        return exposedObject;
    }

    /**
     * 获取bean的早期引用，生成代理后的bean
     */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) bp).getEarlyBeanReference(exposedObject, beanName);
                if (exposedObject == null) {
                    return null;
                }
            }
        }
        return exposedObject;
    }


    /**
     * 实例化前的特殊BeanPostProcessor处理
     * @return 如果bean需要代理，直接返回代理对象；否则返回null
     */
    protected Object applyBeanPostProcessorsBeforeInstance(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstance(beanClass, beanName);
                // 返回结果不为null，代表执行了代理操作，直接返回代理对象
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 实例化后的特殊BeanPostProcessor处理
     * @return false表示不继续执行后续的属性填充逻辑；true表示继续放行
     */
    private boolean applyBeanPostProcessorsAfterInstance(String beanName, Object bean) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                if (!((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstance(bean, beanName)) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 调用实例化策略实例化bean
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    /**
     * 在set方法应用属性值之前，执行BeanPostProcessor的前置处理，修改属性值
     */
    protected void applyBeanPostprocessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor postProcessor : getBeanPostProcessors()) {
            if (postProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) postProcessor)
                        .postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (pvs != null) {
                    for (PropertyValue pv : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(pv);
                    }
                }
            }
        }
    }

    protected void applyPropertyValues(String beanName, BeanDefinition beanDefinition, Object bean) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues(); // 通过definition获取属性值
            PropertyValue[] arr = propertyValues.getPropertyValues(); // 获取属性值数组

            // 为bean object通过set方法设置属性值
            for (PropertyValue propertyValue : arr) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if (value instanceof BeanReference) {
                    // 处理BeanReference类型的属性值
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                } else {
                    // 类型转换
                    Class<?> sourceType = value.getClass();
                    Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                    ConversionService conversionService = getConversionService();
                    if (conversionService != null) {
                        if (conversionService.canConvert(sourceType, targetType)) {
                            value = conversionService.convert(value, targetType);
                        }
                    }
                }

                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values for bean: " + beanName, e);
        }
    }

    /**
     * 在bean对象实例化后，执行bean对象的初始化方法和BeanPostProcessor
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 为其设置所属的BeanFactory
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        // 执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInit(bean, beanName);

        // 执行bean对象的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable ex) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", ex);
        }

        // 执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInit(wrappedBean, beanName);

        return wrappedBean;
    }

    /**
     * 通过反射，调用definition中的init方法
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {

        // 继承自InitializingBean，带init方法的bean
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 在xml中自定义的init方法
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(), initMethodName);
            if (initMethod == null) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(bean);
        }
    }

    /**
     * 注册有销毁方法的单例bean，即bean继承自DisposableBean或有自定义的销毁方法
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

        // 只有singleton类型bean会执行销毁方法
        if (beanDefinition.isSingleton()) {
            if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
                // 传入适配器，代替没有实现DisposableBean接口的bean，实现销毁方法
                registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition.getDestroyMethodName()));
            }
        }
    }

    /**
     * 初始化前的BeanPostProcessor处理
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInit(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInit(result, beanName);
            // 处理中间产生null，直接返回上一次处理的结果
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInit(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInit(result, beanName);
            // 处理中间产生null，直接返回上一次处理的结果
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
