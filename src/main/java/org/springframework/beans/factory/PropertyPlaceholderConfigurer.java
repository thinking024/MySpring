package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

import java.util.Properties;

/**
 * 实现了BeanFactoryPostProcessor接口，能从properties文件中解析占位符${}，从而修改definition
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    public static final String PLACEHOLDER_PREFIX = "${";
    public static final String PLACEHOLDER_SUFFIX = "}";
    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 解析properties文件
        Properties properties = loadProperties();
        processProperties(beanFactory, properties);

        // 往容器中添加字符解析器，供解析@Value注解使用
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
        beanFactory.addEmbeddedValueResolver(valueResolver);
    }

    private Properties loadProperties() {
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();
            Resource resource = loader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (Exception e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();

        // 遍历definition，解析占位符定义的属性值
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            resolvePropertyValues(beanDefinition, properties);
        }
    }

    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        PropertyValue[] propertyValueArray = propertyValues.getPropertyValues();

        for (PropertyValue propertyValue : propertyValueArray) {
            Object value = propertyValue.getValue();
            if (value instanceof String strVal) {
                value = resolvePlaceholder(strVal, properties);

                // 更换definition中的属性值
                propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
            }
        }
    }

    /**
     * 将${}占位符替换为properties中的值
     * @param strVal 含${}占位符的字符串
     * @param properties properties对象
     * @return 替换后的字符串
     */
    private String resolvePlaceholder(String strVal, Properties properties) {
        StringBuilder buff = new StringBuilder(strVal);
        int startIndex = strVal.indexOf(PLACEHOLDER_PREFIX);
        int endIndex = strVal.indexOf(PLACEHOLDER_SUFFIX);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String propKey = strVal.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
            String propVal = properties.getProperty(propKey);
            buff.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
        }
        return buff.toString();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        public String resolveStringValue(String strVal) throws BeansException {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }
}
