package org.springframework.aop;

/**
 * 包含PointCut和Advice的组合，前者捕获JointPoint，后者执行具体操作
 */
public interface PointcutAdvisor extends Advisor {

	Pointcut getPointcut();
}