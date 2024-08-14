package org.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * ClassPathBeanDefinitionScanner自动扫描@Scope注解，
 * 根据value属性值，设置BeanDefinition的scope属性
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	String value() default "singleton";
}