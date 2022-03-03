package com.yu.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.PropertyValue;
import com.yu.springframework.beans.PropertyValues;
import com.yu.springframework.beans.factory.*;
import com.yu.springframework.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

//实例化bean类
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();
    //实例化bean对象，并调用实例化的bean放入单例对象的缓存中
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try{
            // 判断是否返回代理 Bean 对象
            bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (null != bean) {
                return bean;
            }
            //实例化bean
            bean = createBeanInstance(beanDefinition,beanName,args);
            //在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName,bean,beanDefinition);
            //给bean填充属性
            applyPropertyValues(beanName,bean,beanDefinition);
            //执行bean的初始化方法和BeanPostProcessor 的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        }catch(Exception e){
            throw new BeansException("Instantiation of bean failed", e);
        }
        //
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        //将实例化的bean放入到单例对象中
        if(beanDefinition.isSingleton()){
            addSingleton(beanName, bean);
        }
        return bean;
    }
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition){
        //非Singleton类型的bean 不执行销毁方法
        if(!beanDefinition.isSingleton())return;
        if(bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())){
            registerDisposableBeans(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException {
        Constructor constructorToUse = null;
        //通过反射获取bean的类
        Class<?> beanClass = beanDefinition.getBeanClass();
        //获取bean的类中所有的构造器
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        //遍历构造器判断构造器参数是否相等
        for(Constructor ctor:declaredConstructors){
            if(null!=args&&ctor.getParameterTypes().length==args.length){
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition,beanName,constructorToUse,args);
    }
    public InstantiationStrategy getInstantiationStrategy(){return instantiationStrategy;}
    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy){
        this.instantiationStrategy = instantiationStrategy;
    }
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

        if(bean instanceof Aware){
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if(bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if(bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }
        //1.执行BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        //待完成内容
        try{
            invokeInitMethod(beanName, wrappedBean, beanDefinition);
        }catch (Exception e){
            throw new BeansException("Invocation of init method of bean["+beanName+"] failed", e);
        }
        //2.执行BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;

    }

    //执行bean对象的初始化方法
    private void invokeInitMethod(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        //1、实现接口 Initialization
        if(bean instanceof InitializingBean){
            ((InitializingBean)bean).afterPropertiesSet();
        }
        //2、配置信息 init-method
        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotEmpty(initMethodName)){
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if(null==initMethod){
                throw new BeanException("Could not find an init method named '" +
                        initMethodName + "' on bean with name '" + beanName + "'");
            }
            //反射调用initMethod
            initMethod.invoke(bean);
        }
    }

    /**
     *在设置 bean 属性之前，允许 BeanPostProcessor 修改属性值
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws BeansException
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if(null!=pvs){
                    for(PropertyValue propertyValue: pvs.getPropertyValues()){
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        try{
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for(PropertyValue propertyValue: propertyValues.getPropertyValues()){
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                //循环依赖
                if(value instanceof BeanReference){
                    //A依赖B ,获取 B的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                //属性填充
                BeanUtil.setFieldValue(bean,name,value);
            }
        }catch (Exception | BeansException e){
            throw new BeanException("Error setting property values: "+beanName);
        }
    }
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (null != bean) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (null != result) return result;
            }
        }
        return null;
    }
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor processor:getBeanPostProcessors()){
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if(null == current)return result;
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for(BeanPostProcessor processor:getBeanPostProcessors()){
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if(null == current)return result;
            result = current;
        }
        return result;
    }
}

