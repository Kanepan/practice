/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.kane.practice.newfeature.supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class SupplierTest {
    public static void main(String[] args) {
        Cache cache = new Cache();
        Db db = new Db();
        String key = "1";
        String value = null;
        value = SupplierTest.selectCacheTemplate(
                () -> cache.get(key),
                () -> db.get(key),
                () -> cache.save(key, db.get(key))
        );

        System.out.println(value);
        System.out.println(cache.get("1"));
        cache.save("1", "new value");

//        Function<String,String> getFromDb = e ->{return db.get(key);};
        value = SupplierTest.selectCacheTemplate(
                () -> cache.get(key),
                () -> db.get(key),
                () -> cache.save(key, db.get(key))
        );
        //这里实际会调用两次，不推荐这么写。

        System.out.println(value);

    }


    public static <T> T selectCacheTemplate(CacheSelector<T> cacheSelector, Supplier<T> databaseSelector, CacheSaver cacheSaver) {
        T t = cacheSelector.get();
        if (t == null) {
            t = databaseSelector.get();
            if (t != null) {
                cacheSaver.save();
            }
        }
        return t;
    }

    static class Db {
        public String get(String key) {
            return System.currentTimeMillis() + "";
        }
    }

    static class Cache {
        Map<String, String> map = new HashMap<>();

        public String get(String key) {
            return map.get(key);
        }

        public void save(String key, String value) {
            map.put(key, value);
        }
    }
}
