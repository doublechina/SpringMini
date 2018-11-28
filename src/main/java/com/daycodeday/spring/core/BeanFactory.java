package com.daycodeday.spring.core;

public interface BeanFactory {
    /**
     * 根据beanName从IOC容器之中获取一个实例
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
