package com.yu.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.util.StrUtil;
import com.yu.springframework.beans.factory.DisposableBean;
import com.yu.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition){
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }
    @Override
    public void destroy() throws Exception {
        //1、实现接口 DisposableBean
        if(bean instanceof DisposableBean){
            ((DisposableBean)bean).destroy();
        }

        if(StrUtil.isNotEmpty(destroyMethodName)&&!(bean instanceof DisposableBean && "destory".equals(this.destroyMethodName))){
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if(null == destroyMethod){
                throw new BeanException("Couldn't find a destory method named '" +
                        destroyMethodName + "' on bean with name '"+beanName+"'");
            }
            destroyMethod.invoke(bean);

        }
    }
}
