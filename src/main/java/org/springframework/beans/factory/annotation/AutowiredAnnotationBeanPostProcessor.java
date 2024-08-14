package org.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    /**
     * 处理@Value和@Autowired注解
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有字段
        for (Field field : fields) {
            String fieldName = field.getName();

            // value 注解
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                Object value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue((String) value);

                // 类型转换
                Class<?> sourceType = value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getType(field);
                ConversionService conversionService = beanFactory.getConversionService();
                if (conversionService != null) {
                    if (conversionService.canConvert(sourceType, targetType)) {
                        value = conversionService.convert(value, targetType);
                    }
                }

                BeanUtil.setFieldValue(bean, fieldName, value);
            }

            // autowired 注解
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation != null) {
                Class<?> type = field.getType();
                String dependentBeanName;
                Object dependentBean;
                // qualifier 注解
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                if (qualifierAnnotation != null) {
                    // 有qualifier，byName
                    dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, type);
                } else {
                    // 无qualifier，byType
                    dependentBean = beanFactory.getBean(type);
                }
                BeanUtil.setFieldValue(bean, fieldName, dependentBean);
            }
        }

        return pvs;
    }

    @Override
    public Object postProcessBeforeInstance(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstance(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
