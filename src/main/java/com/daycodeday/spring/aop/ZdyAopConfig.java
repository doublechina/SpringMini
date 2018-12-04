package com.daycodeday.spring.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 只是对application中的expression的封装
 * 目标代理对象的一个方法要增强
 * 用自己实现的业务逻辑去增强
 * 配置文件的目的：告诉Spring那些类的那些方法需要增强，增强的内容是什么
 * 对配置文件中所体现的你诶龙进行封装
 */
public class ZdyAopConfig {
    //以目标对象需要增强的Method作为key，需要增强的代码内容作为value
    private Map<Method, ZdyAspect> pointMap = new HashMap<>();

    public void put(Method target, Object aspect, Method[] points) {
        this.pointMap.put(target, new ZdyAspect(aspect, points));
    }

    public ZdyAspect get(Method method) {
        return this.pointMap.get(method);
    }

    public boolean contains(Method method) {
        return this.pointMap.containsKey(method);
    }
    //对增强的代码的封装
    public class ZdyAspect {
        private Object aspect;//待会见LogAspect这个对象赋值给它
        private Method[] points;//会将LogAspect的before方法和after方法赋值进来

        ZdyAspect(Object aspect, Method[] points) {
            this.aspect = aspect;
            this.points = points;
        }

        public Object getAspect() {
            return aspect;
        }

        public void setAspect(Object aspect) {
            this.aspect = aspect;
        }

        public Method[] getPoints() {
            return points;
        }

        public void setPoints(Method[] points) {
            this.points = points;
        }
    }
}
