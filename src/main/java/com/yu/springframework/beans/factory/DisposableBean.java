package com.yu.springframework.beans.factory;

public interface DisposableBean {
    void destroy() throws Exception;
}
