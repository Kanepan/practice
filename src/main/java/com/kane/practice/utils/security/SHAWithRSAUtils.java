package com.kane.practice.utils.security;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SHAWithRSAUtils {

    private static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrAdmuvRLPJjRYgubWqjsnG9iDkpbbepvLi20fMFRYkXnCEG375U+FPK73PhAgLcRcZoxzVroHmeEhnvK4rTQroC0GzGSUaeXBX29tdMWgNyQsy3i0OL5boQsRxAzR4C7xIs4pC5efrXRSTyVGbc6gSdSnVnjtRCMfOxayXCuOcYGSoYsrLGutb1iw36KmzVcghju+BHEGOWnNIT3C7+VQ3/LoLpc+xfpT+0QcOWjN8XGrmx/TGOdyHKsNI9pRLIEVvHTqT1NeH1H87NMjlSDNP/SXdREVB0DCgnMbI5Yx9+KY897XMPdDWXoZKtodn0crj2/i0hRYevM99IutIM4BAgMBAAECggEAMor4VVOGyxSNZ4fnu71q8XNUsp+BYHzTKwi2lGGhklbfV/SOowFjg+VUQAqSD0molLLdfCTn56CZwdBcpYli0gsmA/NLonvQFTHAVksqAdY6KoQsVp5pmm1dYxnGJBFSJzx0GHHMz2PPw7AP9UDCksiuOrsnUOg5oYrky8F1ALJRN9vljDZOVHCtJHYAq6Qci+oygVuvPCLUHNkuj32FzNf4E+zDDf7KKI/qbSQu7ijmtAo83gvAVOsO6Vr3B+axsWDtSbRV7LQfNXClgo7et2xK7L86vvbuqZ/PmpQOKo4zC6mX4d+jPWYQ3U/DSCqRu+sVM/HKRS1QCtOtFoV3/QKBgQD+Quv/8tNJbrkept+26CeIUJdk3jl8SRsor4NulV4nft0qwrCv+qGGvatYCR1O7JpFIpZinwIZbdMst/gEf1CwF6H6DrHIpVdu1Z88kdi6yZbU66OU0WRVHldo7QTnpiSkyjiFl1G1NLSVmVFr2b1ZZZdhT53N/eGKZbg+Z46X5wKBgQDsnTl390ZTZbA49wkShy73qL419nlp9mtsE2decxNX9WVCmYjkXVYozk5FA+WO0nfxCTp7xvCHkg5lWg8mUXkg/zffcsUygYecp5+hAkzNpN7cMmY+x91yXEEaX77zu+5PjgaIBwzqmrB5tySFkaOGPQb7+eUZyKc6KiKfJQON1wKBgHsm8OYcuEgGEDYPf6Y5FgTJfDrIBDH1uuje8hsaz+rzYK54dFoYXCpGLfrLF14F7cb0tMB54ettLW6ogMG05OEV4Ueb3HKh2xJ8986/makHQT5KEAZh0WYE9zOEMe79oxwKMoxKObI6IAmhpDVH1pW5RjRE+tsuUBLXqADZ6dAbAoGBANChSRzoYZADZ8CywcMeg2FH3n/CVggCiXGAInL7UKEL9T8mFbgWPHQ6hHUCHJU0KyHTG0gsf8VqGVtwb09AI2fe4asajZoc8DBG9M+JMaNZrk/F+LV+kVnW0mio57wNhvGKr2Odo9vwjY6k14UWh3NDuDbRfxqe/CSjJmfgwSN9AoGAEm4ke1BFixo5y4EnhnJ6nVyboAkH24OcH1SgTPhpASeFzsFCCZVmHRvMU2Otuem87xAGFZaCpK6MWIgj6Uj0Lp7Zj2VhLwpr0cncdDa8IFREciEOabYJR7A0ZEDySJgbvf0i0NVeYugbowLBy5h3M2C9IylbCEhjMvJ7JUSyeAA=";
    private static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6wHZrr0SzyY0WILm1qo7JxvYg5KW23qby4ttHzBUWJF5whBt++VPhTyu9z4QIC3EXGaMc1a6B5nhIZ7yuK00K6AtBsxklGnlwV9vbXTFoDckLMt4tDi+W6ELEcQM0eAu8SLOKQuXn610Uk8lRm3OoEnUp1Z47UQjHzsWslwrjnGBkqGLKyxrrW9YsN+ips1XIIY7vgRxBjlpzSE9wu/lUN/y6C6XPsX6U/tEHDlozfFxq5sf0xjnchyrDSPaUSyBFbx06k9TXh9R/OzTI5UgzT/0l3URFQdAwoJzGyOWMffimPPe1zD3Q1l6GSraHZ9HK49v4tIUWHrzPfSLrSDOAQIDAQAB";

    public static void main(String[] args) {
        byte[] data = new byte[0];
        try {
            data = "我是一段测试字符串".getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Sign sign = null;
        byte[] privatekeyBytes = base64String2Byte(privateKey);
        byte[] puglickeyBytes = base64String2Byte(publicKey);
        sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, privatekeyBytes, puglickeyBytes);
        byte[] signed = sign.sign(data);
        System.out.println(byte2Base64String(signed));
        boolean verify = sign.verify(data, signed);
        System.out.println(verify);
    }

    public static String byte2Base64String(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64String2Byte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }
//
//    public static String string(byte[] bytes) {
//        try {
//            return new String(bytes, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static byte[] bytes(String base64Str) {
//        try {
//            return base64Str.getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}