package com.jinniu.interfaces.websocket.Mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * pid与业务方法对应的
 */
public class RouterHelper {

    private static Map<String, Method> methodMap;

    static {
        init();
    }

    public static Method from(int pid) {
        Method m = methodMap.get(pid + "");
        if (m == null) {
            throw new RuntimeException(String.format("未实现pid为[%s]的方法!", pid));
        }
        return m;
    }

    private static void init() {
        methodMap = new HashMap<String, Method>(20);
        Method[] methods = ActionRouter.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Pid.class)) {
                Pid pid = method.getAnnotation(Pid.class);
                methodMap.put(pid.value().getPid() + "", method);
            }
        }
    }


}
