package com.yu.springframework.context;

/**
 * 事件发布接口
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
