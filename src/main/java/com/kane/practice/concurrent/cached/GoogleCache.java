package com.kane.practice.concurrent.cached;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.TimeUnit;

public class GoogleCache {

    private static Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2000, TimeUnit.MILLISECONDS)
            .removalListener((RemovalListener<String, String>) removal -> System.out.println("我的key失效-" + removal.getKey()))
            .build();

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                cache.asMap().putIfAbsent("1", "gggg");
            }
        }).start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object[] keys = cache.asMap().keySet().toArray();

        System.out.println("keySet():" + keys.length);
        System.out.println("size(): " + cache.size());
        System.out.println(cache.getIfPresent("1"));

        System.out.println("获取一次之后");

        //在 cleanup或者获取一次后 之后才会.   google的 cached 是在读写的时候,才会触发失效 expire
//        cache.cleanUp();

        System.out.println(cache.asMap().size());
        System.out.println("keySet():" + keys.length);
    }
}
