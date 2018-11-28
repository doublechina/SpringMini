package com.daycodeday.spring.beans;

import com.daycodeday.spring.core.FactoryBean;

public class BeanWrapper extends FactoryBean{
    /**
     * 还会用到观察者模式
     * 1.支持事件响应，会有一个监听
     * 2.
     */
    private BeanPostProcessor postProcessor;
    private Object wrappedInstance;
    /**
     * 原始的通过反射new出来，要把包装起来，存起来
     */
    private Object originalInstance;
    public BeanWrapper(Object instance) {
        this.wrappedInstance=instance;
        this.originalInstance=instance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    public void setWrappedInstance(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    /**
     * 返回代理以后的class类
     * 可能会是这个$Proxy0
     * @return
     */
    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }

    public BeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(BeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }
}
