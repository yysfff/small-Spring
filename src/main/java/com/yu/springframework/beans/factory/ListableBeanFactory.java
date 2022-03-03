package com.yu.springframework.beans.factory;

import com.yu.springframework.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{
    //按照类型获取bean实例
    <T> Map<String,T> getBeansOfType(Class<T> type) throws BeansException;

    //返回注册表中所有的bean名称
    String[] getBeanDefinitionNames();

}
