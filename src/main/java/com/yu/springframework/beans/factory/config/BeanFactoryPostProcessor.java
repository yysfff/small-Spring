package com.yu.springframework.beans.factory.config;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {
    /**
     * 在所有的BeanDefinition 加载完成后，实例化bean 对象之前，提供修改BeanDefinition 属性的机制
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
