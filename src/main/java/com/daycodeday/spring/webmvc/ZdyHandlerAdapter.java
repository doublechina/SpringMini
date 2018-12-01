package com.daycodeday.spring.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 解耦，专人干专事
 */
public class ZdyHandlerAdapter {
    private Map<String, Integer> paramMapping;

    public ZdyHandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }

    /**
     * @param req
     * @param resp
     * @param handlerMapping 为什么要把handler传进来，因为handler中包含controller、method、url信息
     * @return
     */
    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, ZdyHandlerMapping handlerMapping) throws InvocationTargetException, IllegalAccessException {
        //根据用户请求的参数信息，跟method中参数信息进行动态匹配
        //resp传进来的目的=只有一个:只是为了将其赋值给方法参数，仅此而已
        //只有当用户传进来的ModeAndVie为空的时候，才会new一个默认的
        //1.要准备好这个方法的形参列表
        //方法重载：形参的决定因素，参数的个数，参数的类型，参数的顺序，方法的名字
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        //2.拿到自定义的命名参数列表所在的位置
        //用户通过URL传过来的参数列表
        Map<String, String[]> reqParamValues = req.getParameterMap();
        //3.构造实参内部
        Object[] paramValues = new Object[paramTypes.length];
        for (Map.Entry<String, String[]> param : reqParamValues.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (!this.paramMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = this.paramMapping.get(param.getKey());
            //因为页面上传过来的值都是String类型的，而在方法中定义的类型是千变万化的
            //要针对我们传过来的参数进行类型转换
            paramValues[index] = caseStringValue(value, paramTypes[index]);

        }
        if (this.paramMapping.containsKey(HttpServletRequest.class.getName())){
            int reqIndex = this.paramMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = req;
        }
        if (this.paramMapping.containsKey(HttpServletResponse.class.getName())){
            int resqIndex = this.paramMapping.get(HttpServletResponse.class.getName());
            paramValues[resqIndex] = resp;
        }
        //4.从handler中取出controller、method,然后利用反射机制进行调用

        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        if (result == null) return null;
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if (isModelAndView) {
            return (ModelAndView) result;
        } else {
            return null;
        }

    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        } else {
            return null;
        }


    }
}
