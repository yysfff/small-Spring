package com.yu.springframework.beans.factory.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.BeanFactory;
import com.yu.springframework.beans.factory.FactoryBean;
import com.yu.springframework.beans.factory.config.BeanDefinition;
import com.yu.springframework.beans.factory.config.BeanPostProcessor;
import com.yu.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.yu.springframework.utils.ClassUtils;
import com.yu.springframework.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

//bean工厂的抽象类，实现接口BeanFactory的getBean()方法
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();
    //获取bean，首先调用DefaultSingletonBeanRegistry中的getSingleton()方法,获取bean的单例实现，其中单例模式是为了防止同一bean的多次实例化。
    //如果该bean没有实例化，则先调用getBeanDefinition()方法获取bean的定义然后调用createBean()实例化bean
    //其中getBeanDefinition()和createBean()均为抽象方法，由其子类实现。
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name,null);
    }
    @Override
    public Object getBean(String name, Object... args) throws BeansException{
        return doGetBean(name,args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }
    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }
    protected <T> T doGetBean(final String name, final Object[] args) throws BeansException {
        Object sharedInstance = getSingleton(name);
        if(sharedInstance!=null){
            return (T)getObjectForBeanInstance(sharedInstance, name);
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name,beanDefinition,args);
        return (T) getObjectForBeanInstance(bean, name);
    }
    private Object getObjectForBeanInstance(Object beanInstance, String beanName){
        if(!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }
        Object object = getCacheObjectForFactoryBean(beanName);

        if(object == null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return object;
    }
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }
    public List<BeanPostProcessor> getBeanPostProcessors(){
        return this.beanPostProcessors;
    }
    public ClassLoader getBeanClassLoader(){
        return this.beanClassLoader;
    }
}
