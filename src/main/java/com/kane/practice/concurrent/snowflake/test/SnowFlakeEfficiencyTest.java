package com.kane.practice.concurrent.snowflake.test;

import com.kane.practice.concurrent.snowflake.ShortBizUidGenerator;
import com.kane.practice.concurrent.snowflake.ShortUidGenerator;

public class SnowFlakeEfficiencyTest {
//    private static final DefaultUidGenerator uidGenerator = new DefaultUidGenerator();

    private static final ShortUidGenerator uidGenerator = new ShortUidGenerator();
    private static final ShortUidGenerator uidGenerator1 = new ShortUidGenerator();
    private static final ShortUidGenerator uidGenerator2 = new ShortUidGenerator();


    private static final ShortBizUidGenerator shortBizUidGenerator = new ShortBizUidGenerator();


    static{
        try {
            shortBizUidGenerator.init();
            uidGenerator.afterPropertiesSet();
            uidGenerator1.afterPropertiesSet();
            uidGenerator2.afterPropertiesSet();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test1(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000000; i++) {
                    Long id = uidGenerator.getUID();
//                    System.out.println(id);
//                    System.out.println(uidGenerator.parseUID(id));
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000000; i++) {
                    Long id = uidGenerator1.getUID();
//                    System.out.println(id);
//                    System.out.println(uidGenerator.parseUID(id));
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000000000; i++) {
                    Long id = uidGenerator2.getUID();
//                    System.out.println(id);
//                    System.out.println(uidGenerator.parseUID(id));
                }
            }
        }).start();


    }


    public static void test2(){
        String order = "order";
        String payOrder = "pay";
        String refund = "refund";
        int count = 1000000000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    Long id = shortBizUidGenerator.getUID(order);
//                    System.out.println(id);
//                    System.out.println(shortBizUidGenerator.parseUID(id)+ "   biz--" + order);
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    Long id = shortBizUidGenerator.getUID(refund);
//                    System.out.println(id);
//                    System.out.println(shortBizUidGenerator.parseUID(id)+ "   biz--" + refund);
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    Long id = shortBizUidGenerator.getUID(payOrder);
//                    System.out.println(id);
//                    System.out.println(shortBizUidGenerator.parseUID(id) + "   biz--" + payOrder);
                }
            }
        }).start();

    }

    public static void main(String[] args) {
        test1();
    }
}
