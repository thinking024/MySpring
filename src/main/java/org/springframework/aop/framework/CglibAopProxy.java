package org.springframework.aop.framework;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.aop.AdvisedSupport;

import java.lang.reflect.Method;

/**
 * Cglib动态代理，传入代理相关的信息，生成代理对象
 * todo jdk16以上不再支持cglib，需要使用bytebuddy，spring官方在spring-core中自己维护了一套cglib
 */
public class CglibAopProxy implements AopProxy {

    private final AdvisedSupport advised;

    /**
     * 构造方法，传入代理相关的信息
     */
    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 通过cglib的Enhancer创建代理对象
     */
    @Override
    public Object getProxy() {
        // todo jdk16以上不再支持cglib，需要使用bytebuddy
        Enhancer enhancer = new Enhancer();

        // 将被代理target设置为父类
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());

        // 将被代理target的所有父接口，设置为代理对象的接口
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());

        // 设置方法拦截器
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    /**
     * 继承自cglib的MethodInterceptor（父类为aop alliance的MethodInterceptor），
     * 用于执行代理方法，基本逻辑同jdk一致
     */
    private static class DynamicAdvisedInterceptor implements net.sf.cglib.proxy.MethodInterceptor {

        private final AdvisedSupport advised;

        public DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            // 通过aop alliance的方式执行代理基本逻辑同jdk一致
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);

            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                // 获取代理方法拦截器
                org.aopalliance.intercept.MethodInterceptor interceptor = advised.getMethodInterceptor();

                // 执行代理方法
                return interceptor.invoke(methodInvocation);
            }

            // 不匹配，通过cglib反射直接执行目标对象的方法
            // 即methodProxy.invoke(advised.getTargetSource().getTarget(), objects)，见下面的intercept2方法
            return methodInvocation.proceed();
        }

        public Object intercept2(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
           if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
               ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, objects);
               org.aopalliance.intercept.MethodInterceptor interceptor = advised.getMethodInterceptor();
               return interceptor.invoke(methodInvocation);
            }
            return methodProxy.invokeSuper(advised.getTargetSource().getTarget(), objects);
        }
    }

    /**
     * 额外套娃，其实可以不要这个类，直接使用ReflectiveMethodInvocation
     * 继承自反射方法调用类，在ReflectiveMethodInvocation的基础上，多了一个MethodProxy参数
     */
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }

        /**
         * 通过cglib的MethodProxy执行代理方法
         */
        @Override
        public Object proceed() throws Throwable {
            return methodProxy.invoke(target, arguments);
        }
    }
}
