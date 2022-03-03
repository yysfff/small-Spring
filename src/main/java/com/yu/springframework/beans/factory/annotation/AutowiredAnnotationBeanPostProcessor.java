package com.yu.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.PropertyValues;
import com.yu.springframework.beans.factory.BeanFactory;
import com.yu.springframework.beans.factory.BeanFactoryAware;
import com.yu.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.yu.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yu.springframework.utils.ClassUtils;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableBeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        //1、处理注解@value
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz)?clazz.getSuperclass():clazz;
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields){
            Value valueAnnotation = field.getAnnotation(Value.class);
            if(null!=valueAnnotation){
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }
        //2、处理注解@Autowired
        for(Field field:declaredFields){
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if(null!=autowiredAnnotation){
                Class<?> fieldType = field.getType();
                String dependentBeanName = null;
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object dependentBean = null;
                if(null!=qualifierAnnotation){
                    dependentBeanName = qualifierAnnotation.value();
                    beanFactory.getBean(dependentBeanName, fieldType);
                }else{
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }
        return pvs;
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }


}
