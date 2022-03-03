package com.yu.springframework.context;

import com.yu.springframework.beans.BeansException;
import com.yu.springframework.beans.factory.Aware;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
