package com.kane.practice.arithmetic;

import org.hashids.Hashids;

public class HashId {

    public static void main(String[] args) {
        Hashids hashids = new Hashids("this is my salt");
        String hash = hashids.encode(12345L);
        System.out.println(hash);
    }
}
