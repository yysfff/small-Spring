package com.yu.springframework.aop;

/**
 * 类匹配类，用于切点找到给定的接口和目标类
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
