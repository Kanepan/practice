package com.kane.practice.io;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class IoBufferDeal {

    public static void main(String[] args) {
        File src = new File("d://2.txt");
        File dest = new File("d://4.txt");
        IoBufferDeal.readCopy(src, dest);
//        IoBufferDeal.streamCopy(new File("d://555.jpg"),new File("d://dest.jpg"));
        IoBufferDeal.streamCopy(src, dest);

        try {
            dest = new File("d://5.txt");
            FileUtils.copyFile(src, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void streamCopy(File src, File dest) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest))) {
            int length;
            byte[] bytes = new byte[1024];
            while ((length = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, length);
            }
            bos.flush();
            // 1.close()时会自动flush
            // 2.在不调用close()的情况下，缓冲区不满，又需要把缓冲区的内容写入到文件或通过网络发送到别的机器时，才需要调用flush
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readCopy(File src, File dest) {

        try (
                BufferedReader br = new BufferedReader(new FileReader(src));
                BufferedWriter bw = new BufferedWriter(new FileWriter(dest))) {
            //如果手动关闭流，则需要关闭最高层的 BufferReader 和 BufferedWriter ，底层的FileReader和FileWriter自动会关闭

            String str;
            while ((str = br.readLine()) != null) {
                bw.write(str);
                bw.newLine();
            }

        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
