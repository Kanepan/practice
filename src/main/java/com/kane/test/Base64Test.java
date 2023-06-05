package com.kane.test;



import cn.hutool.core.codec.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = Base64.decodeStr("aHR0cDovL2Jrc2Vjb3NzZHBia3N0bXRxdWVyeS1kZXYtb3NzLWVhMTMzLXNxYS5jbi1oYW5nemhvdS1hbGlwYXktZS5vc3MtYWxpcGF5LmFsaXl1bmNzLmNvbS9FU0lHTi9ia3N0bXRjb3JlLzIwMjMwNjAyLzIwMjMwNjAyMjAyNjAwMDA5OTY3NzZfc2VhbGVkLnBkZj9FeHBpcmVzPTE2ODU3MDYyOTAmT1NTQWNjZXNzS2V5SWQ9TFRBSTV0NzVZU3NBSmRQdnhIVXp2NEJ3JlNpZ25hdHVyZT12WDRlRTJFQXQwZTV5bVhUNUxwMHM2Y2xKMmclM0Q=", "UTF-8");
        System.out.println(str);
    }
}
