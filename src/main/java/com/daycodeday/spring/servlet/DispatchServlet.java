package com.daycodeday.spring.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchServlet extends HttpServlet {
    private Properties contextConfig = new Properties();
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private List<String> beanNames = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("---------调用doPost-----------");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //开始初始化的进程
        //定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //加载
        doScanner();
        //注册
        doRegister();
        //自动依赖注入
        doAutowired();
        //如果是SpringMVC会多设计一个HandlerMapping
        //将@RequestMapping中配置的URL和一个Methode关联上
        //以便于从浏览器获取用户输入的url，以便找到具体执行的Method
        initHandlerMapping();
    }

    private void initHandlerMapping() {

    }

    private void doAutowired() {

    }

    private void doRegister() {

    }

    private void doScanner() {

    }

    private void doLoadConfig(String location) {
        //在spring中是通过Reader去查找和定位对不对
        InputStream is=this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:",""));
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
