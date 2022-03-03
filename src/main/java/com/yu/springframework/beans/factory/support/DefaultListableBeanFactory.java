package com.yu.springframework.beans.factory.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.yu.springframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//该类中的map存放的是未实例化的bean定义
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    //注册bean（未实例化）,BeanDefinitionRegistry接口中的方法
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }
    //获取bean，AbstractBeanFactory中的抽象方法
    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition==null){
            throw new BeansException("No bean named '"+ beanName + "'is defined");
        }
        return beanDefinition;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        for (String s : beanDefinitionMap.keySet()) {
            getBean(s);
        }
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                try {
                    result.put(beanName, (T) getBean(beanName));
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        });
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }


    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (1 == beanNames.size()) {
            return getBean(beanNames.get(0), requiredType);
        }

        throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }
}
