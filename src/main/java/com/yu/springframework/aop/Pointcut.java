package com.yu.springframework.aop;

/**
 * 切点表达式
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();
}
