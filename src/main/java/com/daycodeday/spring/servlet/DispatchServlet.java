package com.daycodeday.spring.servlet;

import com.daycodeday.demo.mvc.action.DemoAction;
import com.daycodeday.spring.annotation.Autowrited;
import com.daycodeday.spring.annotation.Controller;
import com.daycodeday.spring.annotation.Service;
import com.daycodeday.spring.context.ZdyApplicationContext;
import com.daycodeday.spring.webmvc.ModelAndView;
import com.daycodeday.spring.webmvc.ZdyHandlerAdapter;
import com.daycodeday.spring.webmvc.ZdyHandlerMapping;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DispatchServlet extends HttpServlet {
    private Properties contextConfig = new Properties();
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private List<String> classNames = new ArrayList<>();
    private final String LOCATION = "contextConfigLocation";
//    private Map<String, ZdyHandlerMapping> handlerMapping = new HashMap<>();
    //思考一下这样设计的经典之处
    //ZdyHandlerMapping 最核心的设计，也是最经典的
    //它牛逼到直接干掉了Struts,WebWork等MVC框架
    private List<ZdyHandlerMapping> handlerMappings=new ArrayList<>();

    private List<ZdyHandlerAdapter> handlerAdapters=new ArrayList<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("---------调用doPost-----------");
//        String url = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        url = url.replace(contextPath, "").replaceAll("/+", "");
//        ZdyHandlerMapping handler = handlerMapping.get(url);
//        try {
//            ModelAndView mv = (ModelAndView) handler.getMethod().invoke(handler.getController());
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

        //对象.方法名才能调用
        //对象要从IOC容器中获取
//        method.invoke(context)
//        doDispatch(req,resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        ZdyHandlerMapping handlerMapping = getHandler(req);
        ZdyHandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        ModelAndView mv = handlerAdapter.handle(req, resp, handlerMapping);
        processDispatchResult(resp, mv);

    }

    private void processDispatchResult(HttpServletResponse resp, ModelAndView mv) {
        //TODO 调用ViewResolver的resolveView方法

    }

    private ZdyHandlerAdapter getHandlerAdapter(ZdyHandlerMapping handlerMapping) {
        return null;
    }

    private ZdyHandlerMapping getHandler(HttpServletRequest req) {
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
//        initSpring(config);
        //相当于把IOC容器初始化了
        ZdyApplicationContext context = new ZdyApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(ZdyApplicationContext context) {
        //有九个策略模式
        //针对每个用户请求，都会经过一些处理的策略之后，最终才能有结果输出
        //每种策略都可以自定义干预，但是最终的结果都是一致
        //最终返回ModeAndView
        //==========这里说的就是传说中九大组件
        //文件上传解析，如果请求类型是multipart件
        initMultipartResolver(context);
        //本地化解析
        initLocaleResolver(context);
        //主题解析
        initThemeResolver(context);

        //我们自己实现
        //HandlerMappings用来保存Controller中配置的RequestMapping和Method的一个对应关系
        initHandlerMappings(context);

        //我们自己实现
        //HandlerAdapters用来动态匹配Method参数，包含类转换，动态负责
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);

        //我们自己实现
        //自己解析一套模板语言
        initViewResolvers(context);
        initFlashMapManager(context);
//        context.getBean()
    }

    private void initFlashMapManager(ZdyApplicationContext context) {

    }

    private void initViewResolvers(ZdyApplicationContext context) {

    }

    private void initRequestToViewNameTranslator(ZdyApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(ZdyApplicationContext context) {

    }

    private void initHandlerAdapters(ZdyApplicationContext context) {

    }

    /**
     * HandlerMappings用来保存Controller中配置的RequestMapping和Method的一一对应
     *
     * @param context
     */
    private void initHandlerMappings(ZdyApplicationContext context) {
        //按照我们通常的理解应该是一个Map
        //Map<String,Method> map;
        //map.put(url,Method)

    }

    private void initThemeResolver(ZdyApplicationContext context) {

    }

    private void initLocaleResolver(ZdyApplicationContext context) {

    }

    private void initMultipartResolver(ZdyApplicationContext context) {
    }


    private void initSpring(ServletConfig config) {
        //开始初始化的进程
        //定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //加载
        doScanner(contextConfig.getProperty("scanPackage"));
        //注册
        doRegister();
        //自动依赖注入
        //在Spring中是通过调用getBean方法才出发依赖注入的
        doAutowired();
        DemoAction action = (DemoAction) beanMap.get("demoAction");
        action.query(null, null, "java");
        //如果是SpringMVC会多设计一个HandlerMapping
        //将@RequestMapping中配置的URL和一个Methode关联上
        //以便于从浏览器获取用户输入的url，以便找到具体执行的Method
        initHandlerMapping();
    }

    private void initHandlerMapping() {

    }

    private void doAutowired() {
        if (beanMap.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowrited.class)) {
                    continue;
                }
                Autowrited autowrited = field.getAnnotation(Autowrited.class);
                String beanName = autowrited.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                //设置强制访问
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), beanMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegister() {
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className :
                    classNames) {
                Class<?> clazz = Class.forName(className);
                //在Spring中用的多个子方法来处理的
                //parseArray，parseMap
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    //在Spring中的这个阶段不是不会直接put instance,这里put的是BeanDefinition
                    beanMap.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    //默认用类名首字母注入
                    //如果自己定义 了beanName，那么优先使用自己定义的beanName
                    //如果是一个接口，使用接口的类型去自动注入
                    //在Spring中同样会分别调用不同的方法 autowritedByName ,autowritedByType
                    String beanName = service.value();
                    if ("".equals(beanName.trim())) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    beanMap.put(beanName, instance);
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        beanMap.put(i.getName(), instance);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file :
                classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private void doLoadConfig(String location) {
        //在spring中是通过Reader去查找和定位对不对
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
