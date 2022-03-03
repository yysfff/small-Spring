package com.yu.springframework.beans.factory.config;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.HierarchicalBeanFactory;
import com.yu.springframework.utils.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory,SingleBeanRegistry {
    //单例模式，创建对象放在内存中
    String SCOPE_SINGLETON = "singleton";
    //原型模式，创建对象不放在内存中，每次重新创建对象
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);
    /**
     * 销毁单例对象
     */
    void destroySingletons();
}
