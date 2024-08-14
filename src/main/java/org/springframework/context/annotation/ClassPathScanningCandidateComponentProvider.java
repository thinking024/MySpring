package org.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫描指定包下的所有类，
 * 返回有Component注解的类的BeanDefinition
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 返回有org.springframework.stereotype.Component注解的类
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        ClassUtil.scanPackageByAnnotation(basePackage, Component.class).forEach(clazz -> {
            candidates.add(new BeanDefinition(clazz));
        });
        return candidates;
    }
}
