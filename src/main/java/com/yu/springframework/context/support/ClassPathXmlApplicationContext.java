package com.yu.springframework.context.support;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.ConfigurableListableBeanFactory;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    private String[] configLocations;
    public ClassPathXmlApplicationContext(){
    }

    public ClassPathXmlApplicationContext(String configLocations) throws BeansException {
        this(new String[]{configLocations});
    }

    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }

    public String[] getConfigLocations() {
        return configLocations;
    }

}
