package com.daycodeday.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解耦，专人干专事
 */
public class ZdyHandlerAdapter {
    /**
     * @param req
     * @param resp
     * @param handlerMapping 为什么要把handler传进来，因为handler中包含controller、method、url信息
     * @return
     */
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, ZdyHandlerMapping handlerMapping) {
        //根据用户请求的参数信息，跟method中参数信息进行动态匹配
        //resp传进来的目的=只有一个:只是为了将其赋值给方法参数，仅此而已
        //只有当用户传进来的ModeAndVie为空的时候，才会new一个默认的
        return null;
    }
}
