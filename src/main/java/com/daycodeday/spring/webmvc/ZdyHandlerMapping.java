package com.daycodeday.spring.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class ZdyHandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;

    public ZdyHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
