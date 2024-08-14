package org.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

/**
 * 在扫描指定包下的所有类的基础上，
 * 将Component注解的类的definition注册到容器中
 * 并且注册负责@Autowired和@Value的BeanPostProcessor
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    private BeanDefinitionRegistry registry;
    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String[] basePackages) {
        for (String basePackage : basePackages) {
            for (BeanDefinition candidate : findCandidateComponents(basePackage)) {
                // 设置作用域
                String beanScope = resolveBeanScope(candidate.getBeanClass());
                if (StrUtil.isNotEmpty(beanScope)) {
                    candidate.setScope(beanScope);
                }

                // 设置bean的名称
                String beanName = determineBeanName(candidate.getBeanClass());
                registry.registerBeanDefinition(beanName, candidate);
            }
        }

        //注册处理@Autowired和@Value注解的BeanPostProcessor
        registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    /**
     * 解析Bean的作用域
     * @param beanClass Bean的Class对象
     * @return 如果有Scope注解，则返回Scope注解的值，否则返回空字符串
     */
    private String resolveBeanScope(Class<?> beanClass) {
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 确定Bean的名称
     * @param beanClass Bean的Class对象
     * @return 如果有Component注解的value属性，则返回value属性的值，否则返回类名的首字母小写
     */
    private String determineBeanName(Class<?> beanClass) {
        String value = beanClass.getAnnotation(Component.class).value();
        if (StrUtil.isEmpty(value)) {
            return StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }
}
