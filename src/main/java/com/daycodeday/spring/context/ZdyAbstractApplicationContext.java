package com.daycodeday.spring.context;

public abstract class ZdyAbstractApplicationContext {
    /**
     * 提供给子类重写的
     */
    protected void onRefresh() {

    }

    protected abstract void refreshBeanFactory();
}
