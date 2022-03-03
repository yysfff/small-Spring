package com.yu.springframework.beans.factory;
/**
 * 感知所属的BeanName
 */
public interface BeanNameAware extends Aware{
    void setBeanName(String name);
}
