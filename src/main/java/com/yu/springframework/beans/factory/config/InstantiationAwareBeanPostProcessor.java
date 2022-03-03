package com.yu.springframework.beans.factory.config;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;
}
