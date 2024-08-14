package org.springframework.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 用于AspectJ的切点表达式
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

	/**
	 * 支持的切点原语
	 */
	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

	// 默认添加了execution原语
	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
	}

	private final PointcutExpression pointcutExpression;

	public AspectJExpressionPointcut(String expression) {
		PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
		pointcutExpression = pointcutParser.parsePointcutExpression(expression);
	}

	/**
	 * 判断是否匹配类的切点表达式
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		return pointcutExpression.couldMatchJoinPointsInType(clazz);
	}

	/**
	 * 判断是否匹配类+方法的切点表达式
	 */
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
	}

	@Override
	public ClassFilter getClassFilter() {
		return this;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}
}