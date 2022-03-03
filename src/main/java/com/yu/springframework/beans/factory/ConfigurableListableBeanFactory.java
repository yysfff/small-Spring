package com.yu.springframework.beans.factory;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.yu.springframework.beans.factory.config.BeanDefinition;
import com.yu.springframework.beans.factory.config.BeanPostProcessor;
import com.yu.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory , AutowireCapableBeanFactory, ConfigurableBeanFactory {
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    void preInstantiateSingletons() throws BeansException;
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
