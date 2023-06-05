package com.kane.practice.base;

public class ShiftTest {

    public static void main(String[] args) {
        //

        Long id = 100l;
        print(id);
//        print(id << 1);
//        print(id >> 1);


        print(id >>> 1);

        int longall = 64;
        int length = 7;

        long id2 = id << (longall - length + 1);
        print(id2);
        print(id2 >>> (longall - 3));

    }

    private static void print(Long id){
        System.out.println(Long.toBinaryString(id.longValue()));
    }
}
