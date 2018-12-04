package com.daycodeday.spring.context;

import com.daycodeday.spring.beans.ZdyBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZdyDefaultListableBeanFactory extends ZdyAbstractApplicationContext{
    //beanDefinitionMap用来保存配置信息
    protected Map<String, ZdyBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    protected void onRefresh() {

    }

    @Override
    protected void refreshBeanFactory() {

    }
}
