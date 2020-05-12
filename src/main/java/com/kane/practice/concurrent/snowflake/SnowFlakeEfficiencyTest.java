package com.kane.practice.concurrent.snowflake;

public class SnowFlakeEfficiencyTest {
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
        for (int i = 0; i < 100000; i++) {
            Long id = uidGenerator.getUID();
            System.out.println(id);
            System.out.println(uidGenerator.parseUID(id));
        }
    }
}
