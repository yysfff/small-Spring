package com.yu.springframework.beans.factory;

public interface InitializingBean {
    /**
     * Bean 处理类属性填充后调用
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
