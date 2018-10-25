package com.kane.practice.io;

import java.io.*;

public class BaseFileDeal {

    public static void main(String[] args) {
      //  BaseFileDeal.readFile("d:\\1.txt");

        BaseFileDeal.writeFile("d:\\2.txt", "1234567");

        BaseFileDeal.copyFile("d:\\2.txt", "d:\\3.txt");
    }

    public static void copyFile(String srcUrl, String targetUrl) {
        try (FileInputStream fis = new FileInputStream(srcUrl); FileOutputStream fos = new FileOutputStream(targetUrl)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileUrl, String text) {
        try (FileOutputStream fos = new FileOutputStream(fileUrl)) {
            byte[] bytes = null;
            bytes = text.getBytes();
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFile(String fileUrl) {
        //File file = new File("d:\\1.txt");
        try (FileInputStream fis = new FileInputStream(fileUrl)) {
            int size = fis.available();
            byte[] bytes = new byte[size];
            fis.read(bytes);
            String result = new String(bytes);
            System.out.println(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
