package com.kane.practice.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ByteArrayOutputStreamTest {
    private static String url = "d://2.txt";
    public static void main(String[] args) {
        readFileByData();
        readFile(url);
        test();
    }

    private static void readFileByData() {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(bout)) {

            String name = "suntao";
            int age = 19;
            dos.writeUTF(name);
            dos.writeInt(age);
           // byte[] buf = bout.toByteArray();//获取内存缓冲区中的数据

//            System.out.println(new String(bout.toByteArray(), "utf-8"));
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bout.toByteArray());
                 DataInputStream dis = new DataInputStream(byteArrayInputStream)) {
                System.out.println(dis.readUTF());
                System.out.println(dis.readInt());
            }
            //readUTF()通常和DataOutputStream的 writeUTF(String str)方法配套使用。  不配套使用经常会报 EOFException
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(String url) {
        try (FileInputStream fis = new FileInputStream(url);
             ByteArrayOutputStream bis = new ByteArrayOutputStream()) {

            byte[] arrayInByte = new byte[1024];
            int len;
            while ((len = fis.read(arrayInByte)) != -1) {
                bis.write(arrayInByte, 0, len);
            }

            System.out.println(new String(bis.toByteArray(), StandardCharsets.UTF_8 ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test() {
        byte[] arr = "hello,大家好!".getBytes();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] a = new byte[1024];
            int len ;
            while ((len = bis.read(a)) != -1) {
                bos.write(a, 0, len);
            }
            //得到ByteArrayOutputStream对象内部数组中的数据
            //将对象内部数组中的数据写入data
            byte[] data = bos.toByteArray();
            System.out.println(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
