/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.daycodeday.spring.aop;


import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;


public class ZdyAopProxyUtils {

    public static Object getTargetObject(Object proxy) throws Exception {
        //先判断一下，这个传进来的这个对象是不是代理过的对象
        //如果不是一个代理对象，就直接返回
        if (!isApoProxy(proxy)) {
            return proxy;
        }
        return getProxyTargetObject(proxy);
    }

    private static boolean isApoProxy(Object proxy) {
        return Proxy.isProxyClass(proxy.getClass());
    }

    private static Object getProxyTargetObject(Object proxy) throws Exception {
        Field h=proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        ZdyAopProxy aopProxy= (ZdyAopProxy) h.get(proxy);
        Field target=aopProxy.getClass().getDeclaredField("target");
        target.setAccessible(true);
        return target.get(aopProxy);
    }
}
