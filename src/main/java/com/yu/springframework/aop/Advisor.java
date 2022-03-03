package com.yu.springframework.aop;

import org.aopalliance.aop.Advice;
//Advisor 访问者
public interface Advisor {

    Advice getAdvice();
}
