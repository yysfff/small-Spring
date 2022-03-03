package com.yu.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import com.yu.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sun.tools.jconsole.inspector.XObject.NULL_OBJECT;

public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCacheObjectForFactoryBean(String beanName){
        Object object = this.factoryBeanObjectCache.get(beanName);
        return (object!=NULL_OBJECT?object:null);
    }

    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName){
        if(factory.isSingleton()){
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object==null){
                object = doGetObjectFromFactory(factory, beanName);
                this.factoryBeanObjectCache.put(beanName,(object!=null?object:NULL_OBJECT));
            }
            return (object != NULL_OBJECT ? object : null);
        }else{
            return doGetObjectFromFactory(factory, beanName);
        }
    }
    private Object doGetObjectFromFactory(final FactoryBean factory, final String beanName){
        try{
            return factory.getObject();
        }catch (Exception e){
            throw new BeanException("FactoryBean throw exception on object[" + beanName + "] creation", e);
        }
    }
}
