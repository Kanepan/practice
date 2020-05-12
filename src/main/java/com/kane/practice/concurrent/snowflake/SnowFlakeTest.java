package com.kane.practice.concurrent.snowflake;

public class SnowFlakeTest {
//    private static final DefaultUidGenerator uidGenerator = new DefaultUidGenerator();

    private static final ShortUidGenerator uidGenerator = new ShortUidGenerator();

    static{
        try {
            uidGenerator.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Long id = uidGenerator.getUID();
        System.out.println(id);
        System.out.println(uidGenerator.parseUID(id));
        id = uidGenerator.getUID();
        System.out.println(id);
        System.out.println(uidGenerator.parseUID(id));
        id = uidGenerator.getUID();
        System.out.println(id);
        System.out.println(uidGenerator.parseUID(id));


        System.out.println(Integer.toBinaryString(1024*8));
        System.out.println(Integer.toBinaryString(31));
    }
}
