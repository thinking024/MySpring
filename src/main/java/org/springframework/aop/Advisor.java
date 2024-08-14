package org.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * Advice的封装
 */
public interface Advisor {

	Advice getAdvice();

}