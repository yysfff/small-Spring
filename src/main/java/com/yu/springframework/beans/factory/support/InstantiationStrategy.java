package com.yu.springframework.beans.factory.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
//实例化策略接口
public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;
}
