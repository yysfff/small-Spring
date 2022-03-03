package com.yu.springframework.beans;

public class BeansException extends Throwable {
    public BeansException(String msg){
        super(msg);
    }
    public BeansException(String msg, Throwable cause){
        super(msg,cause);
    }
}
