package com.yu.springframework.beans.factory;
/**
 * 感知所属的BeanClassLoader
 */
public interface BeanClassLoaderAware extends Aware{
    void setBeanClassLoader(ClassLoader classLoader);
}
