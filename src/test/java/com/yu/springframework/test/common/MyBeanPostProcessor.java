package com.yu.springframework.test.common;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.config.BeanPostProcessor;
import com.yu.springframework.test.bean.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if("userService".equals(beanName)){
//            UserService userService = (UserService) bean;
//            userService.setLocation("改为：北京");
//        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
