package com.daycodeday.spring.context;

import com.daycodeday.demo.mvc.action.MyAction;
import com.daycodeday.spring.annotation.Autowrited;
import com.daycodeday.spring.annotation.Controller;
import com.daycodeday.spring.annotation.Service;
import com.daycodeday.spring.beans.BeanDefinition;
import com.daycodeday.spring.beans.BeanPostProcessor;
import com.daycodeday.spring.beans.BeanWrapper;
import com.daycodeday.spring.context.support.BeanDefinitionReader;
import com.daycodeday.spring.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ZdyApplicationContext implements BeanFactory {
    private String[] configLocations;
    private BeanDefinitionReader reader;
    //beanDefinitionMap用来保存配置信息
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //用来保证注册式单例的容器
    private Map<String, Object> beanCacheMap = new HashMap<>();
    //用来存储所有的被代理过的对象
    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();


    public ZdyApplicationContext(String... locations) {
        this.configLocations = locations;
        refresh();
    }

    public void refresh() {
        //定位
        this.reader = new BeanDefinitionReader(configLocations);
        //加载
        List<String> beanDefinitions = reader.loadBeanDefinitions();
        //注册
        doRegisty(beanDefinitions);
        //依赖注入（lazy-init=false），要执行依赖注入
        //在这里自动调用getBean方法
        doAutorited();

//        MyAction myAction = (MyAction) this.getBean("myAction");
//        myAction.query(null, null, "人性");
    }

    /**
     * 开始执行自动化的依赖注入
     */
    private void doAutorited() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
        //TODO 暴力解决空指针问题
        for (Map.Entry<String, BeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()) {
            populateBean(beanWrapperEntry.getKey(), beanWrapperEntry.getValue().getWrappedInstance());
        }
    }

    public void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        if ((clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowrited.class)) {
                continue;
            }
            Autowrited autowrited = field.getAnnotation(Autowrited.class);
            String autowritedBeanName = autowrited.value().trim();
            if ("".equals(autowritedBeanName)) {
                autowritedBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance, this.beanWrapperMap.get(autowritedBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 真正将beanDefinitions注册到beanDefinitionMap中
     *
     * @param beanDefinitions
     */
    private void doRegisty(List<String> beanDefinitions) {
        //beanName有三种情况
        //1.默认是类名首字母小写
        //2.自定义名字
        //3.接口注入
        try {
            for (String className : beanDefinitions) {
                Class<?> beanClass = Class.forName(className);
                //如果是一个接口，是不能实例化的
                //用它来实现类来实例化
                if (beanClass.isInterface()) {
                    continue;
                }
                BeanDefinition beanDefinition = reader.registerBean(className);
                if (beanDefinition != null) {
                    this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
                }
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没有那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    this.beanDefinitionMap.put(i.getName(), beanDefinition);
                }
                //到这里为止，容器初始化完毕
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 依赖注入，从这里开始，通过读取BeanDefinition中的信息
     * 然后通过反射机制创建一个实例并返回
     * Spring做法是，不会把最原始的对象放进去，会用一个BeanWrapper来进行一次包装
     * 包装器模式：
     * 1.保留原来的OOP关系
     * 2.我需要对他进行扩展，增强(为了以后AOP打基础)
     *
     * @param beanName
     * @return
     */
    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();
        try {
            //生成通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();

            Object instance = instantionBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            //在实例化调用之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            beanWrapper.setPostProcessor(beanPostProcessor);
            this.beanWrapperMap.put(beanName, beanWrapper);
            //TODO 被注入的对象没有实例化，会导致空指针，待解决问题
//            populateBean(beanName,instance);
            //在实例化调用以后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            //通过这样的调用，相当于给自己留有一个可以操作的空间
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 传一个beanDefinition,就返回一个实例Bean
     * TODO 不能保证单例
     *
     * @param beanDefinition
     * @return
     */
    private Object instantionBean(BeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {

            //因为根据Class才能确定一个类时候有实例
            if (this.beanCacheMap.containsKey(className)) {
                instance = this.beanCacheMap.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.beanCacheMap.put(className, instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
//        return getBeanFactory().getBeanDefinitionCount();
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
//        return getBeanFactory().getBeanDefinitionNames();
    }

    public Properties getConfig(){
        return this.reader.getConfig();
    }


}
