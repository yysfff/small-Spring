package com.yu.springframework.beans.factory;

import com.yu.springframework.beans.BeansException;

/**
 * 感知所属的BeanFactory
 */
public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
