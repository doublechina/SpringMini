package com.daycodeday.spring.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 只是对application中的expression的封装
 */
public class ZdyAopConfig {
    private Map<Method, ZdyAspect> pointMap = new HashMap<>();

    public void put(Method target, Object aspect, Method[] points) {
        this.pointMap.put(target, new ZdyAspect(aspect, points));
    }
    public ZdyAspect get(Method method){
        return  this.pointMap.get(method);
    }
    public boolean contains(Method method){
        return this.pointMap.containsKey(method);
    }
    public class ZdyAspect {
        private Object aspect;
        private Method[] points;

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
