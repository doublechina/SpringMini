package com.daycodeday.spring.beans;

import com.daycodeday.spring.aop.ZdyAopConfig;
import com.daycodeday.spring.aop.ZdyAopProxy;
import com.daycodeday.spring.core.FactoryBean;

import java.lang.reflect.Proxy;

public class ZdyBeanWrapper extends FactoryBean {
    private ZdyAopProxy aopProxy = new ZdyAopProxy();
    /**
     * 还会用到观察者模式
     * 1.支持事件响应，会有一个监听
     * 2.
     */
    private ZdyBeanPostProcessor postProcessor;
    private Object wrappedInstance;
    /**
     * 原始的通过反射new出来，要把包装起来，存起来
     */
    private Object originalInstance;

    public ZdyBeanWrapper(Object instance) {
        //从这里开始，我们要把动态代理的代码添加进来了
        this.wrappedInstance = aopProxy.getProxy(instance);
        this.originalInstance = instance;
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
     *
     * @return
     */
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }

    public ZdyBeanPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(ZdyBeanPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }
    public void setAopConfig(ZdyAopConfig config){
        aopProxy.setConfig(config);
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }
}
