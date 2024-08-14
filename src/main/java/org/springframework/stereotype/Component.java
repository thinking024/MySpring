package org.springframework.stereotype;

import java.lang.annotation.*;

/**
 * 无需在xml中定义bean
 * ClassPathBeanDefinitionScanner自动扫描有该注解的类，生成definition并注册到容器中
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

	String value() default "";
}