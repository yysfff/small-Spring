package com.yu.springframework.context.event;

import com.yu.springframework.context.ApplicationEvent;
import com.yu.springframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);
}
