package com.daycodeday.spring.context.support;

import com.daycodeday.spring.beans.ZdyBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 用来对配置文件进行查找，解析，读取
 */
public class BeanDefinitionReader {
    private Properties config = new Properties();
    private List<String> registerBeanClasses = new ArrayList<>();
    //在配置文件中，用来获取自动扫描的包名的key
    private final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String... locations) {
        //在spring中是通过Reader去查找和定位对不对
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        try {
            config.load(is);
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
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    public List<String> loadBeanDefinitions() {
        return this.registerBeanClasses;
    }

    /**
     * 递归扫描所有相关联的class，并且保存到一个List中
     *
     * @param packageName
     */
    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        for (File file :
                classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                registerBeanClasses.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    /**
     * 每注册一个className,就返回一个BeanDefinition，我自己包装
     * 只是为了对配置信息进行一个包装
     *
     * @param className 类名
     * @return
     */
    public ZdyBeanDefinition registerBean(String className) {
        if (this.registerBeanClasses.contains(className)) {
            ZdyBeanDefinition beanDefinition = new ZdyBeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className.substring(className.lastIndexOf(".") + 1)));
            return beanDefinition;
        }
        return null;
    }

    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return this.config;
    }
}
