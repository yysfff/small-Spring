package com.yu.springframework.context.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.yu.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{
    private DefaultListableBeanFactory beanFactory;
    @Override
    protected void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = creatBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory creatBeanFactory(){
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;
    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
