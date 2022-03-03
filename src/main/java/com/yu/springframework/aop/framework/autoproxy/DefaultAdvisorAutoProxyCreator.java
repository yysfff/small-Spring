package com.yu.springframework.aop.framework.autoproxy;

import com.yu.springframework.aop.*;
import com.yu.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.yu.springframework.aop.framework.ProxyFactory;
import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.PropertyValues;
import com.yu.springframework.beans.factory.BeanFactory;
import com.yu.springframework.beans.factory.BeanFactoryAware;
import com.yu.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yu.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass){
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(isInfrastructureClass(bean.getClass()))return bean;
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor:advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if(!classFilter.matches(bean.getClass()))continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = new TargetSource(bean);

            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }
}
