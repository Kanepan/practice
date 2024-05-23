package com.kane.practice.program.logicexecutor.meta;

import cn.hutool.core.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetaContext {

    private static Map<String, String> map = new HashMap<>();

    static {
        String packageName = "com.kane.practice.program.logicexecutor.meta";

        // 创建Reflections对象
        Set<Class<?>> classes = ClassUtil.scanPackageBySuper(packageName, Meta.class);

        // 遍历所有实现类
        for (Class<?> clazz : classes) {
            map.put(clazz.getSimpleName(), clazz.getName());
        }
    }

    public static String getMetaClassName(String metaName) {
        return map.get(metaName);
    }

}
