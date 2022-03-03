package com.yu.springframework.context.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.config.BeanPostProcessor;
import com.yu.springframework.context.ApplicationContext;
import com.yu.springframework.context.ApplicationContextAware;

/**
 * 包装处理器
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ApplicationContext applicationContext;
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
