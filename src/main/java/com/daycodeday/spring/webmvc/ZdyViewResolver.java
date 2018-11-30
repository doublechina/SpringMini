package com.daycodeday.spring.webmvc;

/**
 * 设计这个类的主要目的：
 * 1.将一个静态文件变为一个动态文件
 * 2.根据用户传送的参数不同，产生不同的结果
 * 最终输出字符串，交给Response输出
 */
public class ZdyViewResolver {
    public ZdyViewResolver(String viewName) {
    }

    public String viewResolver(ModelAndView mv) {
        return null;
    }
}
