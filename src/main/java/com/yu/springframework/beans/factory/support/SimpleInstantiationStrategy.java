package com.yu.springframework.beans.factory.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//jdk实例化
public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
        Class clazz = beanDefinition.getBeanClass();
        try{
            //如果ctor为空则为无构造函数实例化
            //getParameterTypes()为获取构造函数的参数类型
            if(null!=ctor){
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            }else{
                return clazz.getDeclaredConstructor().newInstance();
            }
        }catch (NoSuchMethodException|InstantiationException|IllegalAccessException| InvocationTargetException e){
            throw new BeansException("Failed to instantiate ["+clazz.getName()+"]", e);
        }
    }
}
