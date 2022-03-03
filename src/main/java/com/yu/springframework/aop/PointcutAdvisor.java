package com.yu.springframework.aop;

public interface PointcutAdvisor extends Advisor{
    Pointcut getPointcut();
}
