package org.springframework.aop.aspectj;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;
import org.springframework.aop.AdvisedSupport;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.GenericInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.common.WorldServiceBeforeAdvice;
import org.springframework.service.WorldService;
import org.springframework.service.WorldServiceImpl;

import static org.junit.Assert.*;

public class AspectJExpressionPointcutAdvisorTest {
    @Test
    public void testPointCutAdvisor() {

        // 把advice和pointcut封装到pointcut advisor中
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();

        String expression = "execution(* org.springframework.service.WorldService.explode(..))";
        advisor.setExpression(expression);

        GenericInterceptor interceptor = new GenericInterceptor();
        interceptor.setBeforeAdvice(new WorldServiceBeforeAdvice());
        advisor.setAdvice(interceptor); // aop alliance的MethodInterceptor本身就继承自Advice

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();

        // 后续流程和之前一样
        WorldService service = new WorldServiceImpl();
        if (classFilter.matches(service.getClass())) {
            TargetSource targetSource = new TargetSource(service);

            AdvisedSupport support = new AdvisedSupport();
            support.setTargetSource(targetSource);

            // 这里和之前不一样，不再重新new一个MethodInterceptor、MethodMatcher，
            // 而是直接使用advisor的Advice和MethodMatcher信息
            support.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            support.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

            WorldService proxy = (WorldService) new ProxyFactory(support).getProxy();
            proxy.explode();
        }
    }

}