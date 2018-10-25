package com.kane.practice.concurrent;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadContextSwitchTest {
    private static final ArrayBlockingQueue<Long>  queue = new ArrayBlockingQueue<Long>(5000);

    private static AtomicLong count = new AtomicLong();

    private static final ExecutorService es = Executors.newFixedThreadPool(2000);

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            es.submit(new Thread(new ComsumerRunnable(),"消费线程" + i));
        }

        producer();
    }

    static class ComsumerRunnable implements  Runnable{
        public void run() {
            while(true){
                try{
                    Long l = queue.take();
                    //System.out.println(l.toString());
                    doSync();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }



    public static void producer(){
        while(true){
            try{
                boolean flag = queue.offer(count.incrementAndGet());
                if(!flag){
//                    System.out.println(count.get() + "入队失败");
                }
//                Thread.sleep(1);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }



    public static void doSync(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
