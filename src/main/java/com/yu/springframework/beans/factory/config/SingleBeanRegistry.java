package com.yu.springframework.beans.factory.config;
//单例对象
public interface SingleBeanRegistry {
    Object getSingleton(String beanName);
    void registerSingleton(String beanName, Object singletonObject);
}
