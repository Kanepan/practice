package com.kane.practice.utils.security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SHAWithRSAUtils3 {
    private static final Logger logger = LoggerFactory.getLogger(SHAWithRSAUtils.class);

    public static void main(String[] args) throws Exception {
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDrAdmuvRLPJjRYgubWqjsnG9iDkpbbepvLi20fMFRYkXnCEG375U+FPK73PhAgLcRcZoxzVroHmeEhnvK4rTQroC0GzGSUaeXBX29tdMWgNyQsy3i0OL5boQsRxAzR4C7xIs4pC5efrXRSTyVGbc6gSdSnVnjtRCMfOxayXCuOcYGSoYsrLGutb1iw36KmzVcghju+BHEGOWnNIT3C7+VQ3/LoLpc+xfpT+0QcOWjN8XGrmx/TGOdyHKsNI9pRLIEVvHTqT1NeH1H87NMjlSDNP/SXdREVB0DCgnMbI5Yx9+KY897XMPdDWXoZKtodn0crj2/i0hRYevM99IutIM4BAgMBAAECggEAMor4VVOGyxSNZ4fnu71q8XNUsp+BYHzTKwi2lGGhklbfV/SOowFjg+VUQAqSD0molLLdfCTn56CZwdBcpYli0gsmA/NLonvQFTHAVksqAdY6KoQsVp5pmm1dYxnGJBFSJzx0GHHMz2PPw7AP9UDCksiuOrsnUOg5oYrky8F1ALJRN9vljDZOVHCtJHYAq6Qci+oygVuvPCLUHNkuj32FzNf4E+zDDf7KKI/qbSQu7ijmtAo83gvAVOsO6Vr3B+axsWDtSbRV7LQfNXClgo7et2xK7L86vvbuqZ/PmpQOKo4zC6mX4d+jPWYQ3U/DSCqRu+sVM/HKRS1QCtOtFoV3/QKBgQD+Quv/8tNJbrkept+26CeIUJdk3jl8SRsor4NulV4nft0qwrCv+qGGvatYCR1O7JpFIpZinwIZbdMst/gEf1CwF6H6DrHIpVdu1Z88kdi6yZbU66OU0WRVHldo7QTnpiSkyjiFl1G1NLSVmVFr2b1ZZZdhT53N/eGKZbg+Z46X5wKBgQDsnTl390ZTZbA49wkShy73qL419nlp9mtsE2decxNX9WVCmYjkXVYozk5FA+WO0nfxCTp7xvCHkg5lWg8mUXkg/zffcsUygYecp5+hAkzNpN7cMmY+x91yXEEaX77zu+5PjgaIBwzqmrB5tySFkaOGPQb7+eUZyKc6KiKfJQON1wKBgHsm8OYcuEgGEDYPf6Y5FgTJfDrIBDH1uuje8hsaz+rzYK54dFoYXCpGLfrLF14F7cb0tMB54ettLW6ogMG05OEV4Ueb3HKh2xJ8986/makHQT5KEAZh0WYE9zOEMe79oxwKMoxKObI6IAmhpDVH1pW5RjRE+tsuUBLXqADZ6dAbAoGBANChSRzoYZADZ8CywcMeg2FH3n/CVggCiXGAInL7UKEL9T8mFbgWPHQ6hHUCHJU0KyHTG0gsf8VqGVtwb09AI2fe4asajZoc8DBG9M+JMaNZrk/F+LV+kVnW0mio57wNhvGKr2Odo9vwjY6k14UWh3NDuDbRfxqe/CSjJmfgwSN9AoGAEm4ke1BFixo5y4EnhnJ6nVyboAkH24OcH1SgTPhpASeFzsFCCZVmHRvMU2Otuem87xAGFZaCpK6MWIgj6Uj0Lp7Zj2VhLwpr0cncdDa8IFREciEOabYJR7A0ZEDySJgbvf0i0NVeYugbowLBy5h3M2C9IylbCEhjMvJ7JUSyeAA=";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6wHZrr0SzyY0WILm1qo7JxvYg5KW23qby4ttHzBUWJF5whBt++VPhTyu9z4QIC3EXGaMc1a6B5nhIZ7yuK00K6AtBsxklGnlwV9vbXTFoDckLMt4tDi+W6ELEcQM0eAu8SLOKQuXn610Uk8lRm3OoEnUp1Z47UQjHzsWslwrjnGBkqGLKyxrrW9YsN+ips1XIIY7vgRxBjlpzSE9wu/lUN/y6C6XPsX6U/tEHDlozfFxq5sf0xjnchyrDSPaUSyBFbx06k9TXh9R/OzTI5UgzT/0l3URFQdAwoJzGyOWMffimPPe1zD3Q1l6GSraHZ9HK49v4tIUWHrzPfSLrSDOAQIDAQAB";

        String input = "sample input";
        String siged = sign(input, privateKey);
        System.out.println(siged);
        System.out.println(verify(input, siged, publicKey));
    }


    private static final ConcurrentMap<String, Key> keyMap = new ConcurrentHashMap<String, Key>();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String SHA256WITHRSA = "SHA256withRSA";

    public static String sign(String Data, String privateKey) {
        try {
            PrivateKey priKey = null;
            Key key = keyMap.get(privateKey);
            if (key == null) {
                byte[] keyBytes = base64String2Byte(privateKey);
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
                try {
                    KeyFactory keyFactory = null;
                    keyFactory = KeyFactory.getInstance("RSA", "BC");
                    priKey = keyFactory.generatePrivate(pkcs8KeySpec);
                } catch (Exception e) {
                    logger.error("私钥转化出错", e);
                }

                if (priKey != null) {
                    key = priKey;
                    keyMap.putIfAbsent(privateKey, key);
                }
            }
            if (key == null) {
                return null;
            }
            priKey = (PrivateKey) key;
            byte[] data = Data.getBytes();
            Signature signature = Signature.getInstance(SHA256WITHRSA);//这个根据需求填充SHA1WithRSA或SHA256WithRSA
            signature.initSign(priKey);
            signature.update(data);
            return byte2Base64String(signature.sign());
        } catch (Exception e) {
            logger.error("签名失败", e);
            return null;
        }
    }

    public static String byte2Base64String(byte[] bytes) {
        return Base64.encodeBase64String(bytes);

    }

    public static byte[] base64String2Byte(String base64Str) {
        return Base64.decodeBase64(base64Str);
    }

    public static boolean verify(String Data_ori, String Singnature, String publicKey) {
        try {

            PublicKey pubKey = null;
            Key key = keyMap.get(publicKey);
            if (key == null) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64String2Byte(publicKey));
                KeyFactory keyFactory = null;
                try {
                    keyFactory = KeyFactory.getInstance("RSA", "BC");
                    pubKey = keyFactory.generatePublic(keySpec);
                } catch (Exception e) {
                    logger.error("公钥转化出错", e);
                }

                if (pubKey != null) {
                    key = pubKey;
                    keyMap.putIfAbsent(publicKey, key);
                }
            }
            if (key == null) {
                return false;
            }
            pubKey = (PublicKey) key;
            byte[] signed = base64String2Byte(Singnature);

            Signature signature2 = Signature.getInstance(SHA256WITHRSA);//这个根据需求填充SHA1WithRSA或SHA256WithRSA
            signature2.initVerify(pubKey);
            signature2.update(Data_ori.getBytes("UTF-8"));
            boolean verify = signature2.verify(signed);
            return verify;
        } catch (Exception e) {
            logger.error("验签失败", e);
            return false;
        }
    }

}