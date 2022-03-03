package com.yu.springframework.beans.factory;

import com.yu.springframework.beans.BeansException;

//代表了bean对象的工厂，可以存放bean定义到map中以及获取
public interface BeanFactory {
    //获取bean
    Object getBean(String name) throws BeansException;
    Object getBean(String name, Object... args) throws BeansException;
    //根据类型获取bean
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
    <T> T getBean(Class<T> requiredType) throws BeansException;
}
