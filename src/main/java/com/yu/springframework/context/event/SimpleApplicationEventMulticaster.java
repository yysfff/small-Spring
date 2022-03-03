package com.yu.springframework.context.event;

import com.yu.springframework.beans.factory.BeanFactory;
import com.yu.springframework.context.ApplicationEvent;
import com.yu.springframework.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){
        setBeanFactory(beanFactory);
    }
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for(final ApplicationListener listener:getApplicationListeners(event)){
            listener.onApplicationEvent(event);
        }
    }
}
