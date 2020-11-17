package com.kane.practice.arithmetic;

import org.hashids.Hashids;

public class HashId {

    public static void main(String[] args) {
        Hashids hashids = new Hashids("this is my salt");
        String hash = hashids.encode(23245324234L);
        System.out.println(hash);
        long[] a = hashids.decode(hash);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}
