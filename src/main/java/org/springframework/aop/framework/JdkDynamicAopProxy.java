package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理，传入代理相关的信息，生成代理对象
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private AdvisedSupport advised;

    /**
     * 构造方法，传入代理相关的信息
     */
    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }


    /**
     * 通过jdk反射机制创建一个代理对象
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                getClass().getClassLoader(), // 代理对象的类加载器
                advised.getTargetSource().getTargetClass(),  // 目标对象的所有接口，都会被代理对象实现
                this); // 代理对象对应的InvocationHandler（包含invoke方法），用来处理方法的调用
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 在jdk代理的内部通过aop alliance的规范方式执行代理
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            // 执行方法的调用
            ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args);

            // 获取代理方法拦截器
            MethodInterceptor interceptor = advised.getMethodInterceptor();

            // 执行代理方法
            return interceptor.invoke(methodInvocation);
        }

        // 不匹配，通过jdk反射直接执行目标对象的方法
        return method.invoke(advised.getTargetSource().getTarget(), args);
    }
}
