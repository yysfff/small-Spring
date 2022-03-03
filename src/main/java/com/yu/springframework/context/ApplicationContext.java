package com.yu.springframework.context;

import com.yu.springframework.beans.factory.HierarchicalBeanFactory;
import com.yu.springframework.beans.factory.ListableBeanFactory;
import com.yu.springframework.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
