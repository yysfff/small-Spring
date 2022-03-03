package com.yu.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanException;
import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.DisposableBean;
import com.yu.springframework.beans.factory.config.SingleBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//单例对象缓存，放的是实例化后的bean
public class DefaultSingletonBeanRegistry implements SingleBeanRegistry {
    private Map<String, Object> singletonObjects = new HashMap<>();
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    public void registerDisposableBeans(String beanName, DisposableBean bean){
        disposableBeans.put(beanName, bean);
    }
    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName,singletonObject);
    }

    //可以被继承此类的其他类调用
    protected void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName,singletonObject);
    }
    public void destroySingletons(){
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();
        for(int i=disposableBeanNames.length-1;i>=0;i--){
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try{
                disposableBean.destroy();
            } catch (Exception e){
                throw new BeanException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }

        }
    }
}
