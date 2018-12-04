package com.daycodeday.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 默认就用JDK动态代理
 */
public class ZdyAopProxy implements InvocationHandler {
    private ZdyAopConfig config;
    private Object target;

    public ZdyAopProxy() {
    }

    /**
     * 把原生对象传进来
     *
     * @param instance
     * @return
     */
    public Object getProxy(Object instance) {
        this.target = instance;
        Class<?> clazz = instance.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    public void setConfig(ZdyAopConfig config) {
        this.config = config;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        //在原始方法调用以前要执行的增强代码
        if (config.contains(method)) {
            ZdyAopConfig.ZdyAspect aspect = config.get(method);
            aspect.getPoints()[0].invoke(aspect.getAspect());
        }
        //反射调用原始的方法
        Object obj = method.invoke(this.target, objects);
        //在原始方法调用以后要执行的增强代码
        if (config.contains(method)) {
            ZdyAopConfig.ZdyAspect aspect = config.get(method);
            aspect.getPoints()[1].invoke(aspect.getAspect());
        }
        //将最原始的返回值返回出去
        return obj;
    }
}
