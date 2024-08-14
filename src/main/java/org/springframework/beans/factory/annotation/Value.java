package org.springframework.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从配置文件中读取值，通过AutowiredAnnotationBeanPostProcessor前置处理，
 * 在set方法注入前，到Bean中
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Value {

	String value();
}